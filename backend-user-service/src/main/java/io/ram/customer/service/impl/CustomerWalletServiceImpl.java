package io.ram.customer.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import io.ram.config.redisson.RedissonLock;
import io.ram.customer.entity.CustomerWallet;
import io.ram.customer.mapper.CustomerWalletMapper;
import io.ram.customer.service.CustomerWalletService;
import io.ram.domain.req.UpdateBalanceReq;
import io.ram.exception.BizException;
import io.ram.payment.entity.TransferLog;
import io.ram.payment.service.TransferLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static io.ram.customer.entity.table.CustomerWalletTableDef.CUSTOMER_WALLET;

/**
 * 用户钱包表 服务层实现。
 *
 * @author warren
 * @since 2023-08-25
 */
@Service
@Slf4j
public class CustomerWalletServiceImpl extends ServiceImpl<CustomerWalletMapper, CustomerWallet> implements CustomerWalletService {
    @Autowired
    private TransferLogService transferLogService;
    @Autowired
    private CustomerWalletMapper customerWalletMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(value = "updateBalance",key = {"#req.customerId"})
    public void updateBalance(UpdateBalanceReq req) {
        var queryWrapper = QueryWrapper.create().select()
                .from(CUSTOMER_WALLET)
                .where(CUSTOMER_WALLET.CUSTOMER_ID.eq(req.getCustomerId()))
                .and(CUSTOMER_WALLET.CURRENCY.eq(req.getCurrency()));
        CustomerWallet customerWallet = this.mapper.selectOneByQuery(queryWrapper);
        //如果未找到则初始化用户钱包
        if(null==customerWallet){
            customerWallet=CustomerWallet.builder()
                    .currency(req.getCurrency())
                    .balance(BigDecimal.ZERO)
                    .createdBy("System")
                    .customerId(req.getCustomerId())
                    .merchantId(req.getMerchantIds().get(0))
                    .build();
            this.save(customerWallet);
        }

        var oldBalance = customerWallet.getBalance();
        BigDecimal newBalance = req.getTransferAmount().add(oldBalance);
        //如果用户余额小于零则更新失败
        if(newBalance.compareTo(BigDecimal.ZERO)<0){
            throw new BizException.UpdateWalletFailed("update customer balance error,updated balance less then 0");
        }
        customerWallet.setBalance(newBalance);
        int update = customerWalletMapper.update(customerWallet);
        //判断更新行数
        if(update==0){
            throw new BizException.UpdateWalletFailed("update customer wallet error,affected row is zero");
        }
        //插入额度记录
        TransferLog build = TransferLog.builder()
                .customerId(req.getCustomerId())
                .amount(req.getTransferAmount())
                .beforeAmount(oldBalance)
                .afterAmount(newBalance)
                .transferType(req.getTransferType())
                .merchantId(req.getMerchantIds().get(0))
                .build();
        boolean save = transferLogService.save(build);
        //如果额度记录插入失败，则抛出异常，回滚事务
        if(!save){
            throw new BizException.Init("update balance error,insert transfer log failed");
        }
    }

}
