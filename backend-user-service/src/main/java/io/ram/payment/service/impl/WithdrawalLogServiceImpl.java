package io.ram.payment.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import io.ram.config.DateUtil;
import io.ram.config.redisson.RedissonLock;
import io.ram.customer.service.CustomerWalletService;
import io.ram.domain.req.UpdateBalanceReq;
import io.ram.domain.req.fy.withdrawal.SubmitWithdrawalOrderReq;
import io.ram.domain.req.fy.withdrawal.WithdrawalNotifyReq;
import io.ram.domain.req.withdrawal.CreateWithdrawalReq;
import io.ram.domain.resp.fy.SubmitOrderResp;
import io.ram.enums.FyStatus;
import io.ram.enums.TransferType;
import io.ram.enums.WithdrawalStatus;
import io.ram.exception.BizException;
import io.ram.payment.entity.WithdrawalLog;
import io.ram.payment.mapper.WithdrawalLogMapper;
import io.ram.payment.service.FyService;
import io.ram.payment.service.WithdrawalLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static io.ram.payment.entity.table.WithdrawalLogTableDef.WITHDRAWAL_LOG;

/**
 * 取款表 服务层实现。
 *
 * @author warren
 * @since 2023-08-30
 */
@Service
@Slf4j
public class WithdrawalLogServiceImpl extends ServiceImpl<WithdrawalLogMapper, WithdrawalLog> implements WithdrawalLogService {
    @Autowired
    private CustomerWalletService customerWalletService;
    @Autowired
    private FyService fyService;
    @Value("${fy.token}")
    private String fyToken;
    @Value("${fy.merchantId}")
    private String merchantId;
    @Value("${fy.withdrawal.notifyUrl}")
    private String withdrawalNotifyUrl;
    public static final String FAILED = "failed";
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(value = "createWithdrawOrder",key = {"#req.customerId"})
    @Override
    public void createWithdrawalOrder(CreateWithdrawalReq req) {
        //扣除用户余额
        UpdateBalanceReq build = UpdateBalanceReq.builder()
                .customerId(req.getCustomerId())
                .transferAmount(req.getAmount().negate())
                .currency(req.getCurrency())
                .merchantIds(req.getMerchantIds())
                .transferType(TransferType.WITHDRAWAL)
                .build();
         customerWalletService.updateBalance(build);
        LocalDateTime now = DateUtil.getNowByUTC();
        WithdrawalLog withdrawalLog = WithdrawalLog.builder()
                .type(req.getType())
                .cardId(req.getCardId())
                .account(req.getAccount())
                .currency(req.getCurrency())
                .createdBy("System")
                .merchantId(req.getMerchantIds().get(0))
                .customerId(req.getCustomerId())
                .status(WithdrawalStatus.INIT)
                .withdrawalAmount(req.getAmount())
                .createdTime(now)
                .build();
        save(withdrawalLog);
        SubmitOrderResp submitOrderResp = fyService.submitWithdrawOrder(SubmitWithdrawalOrderReq.builder()
                .OrderID(String.valueOf(withdrawalLog.getId()))
                .MerchantID(merchantId)
                .Id(req.getCardId())
                .Account(req.getAccount())
                .Type(req.getType().getCode())
                .NotifyUrl(withdrawalNotifyUrl)
                .MerchantNumber("2002")
                .Date(DateUtil.formatToyyyyMMddHHmmss(withdrawalLog.getCreatedTime()))
                .build());
        if(!CharSequenceUtil.equalsIgnoreCase(submitOrderResp.getCode(),"success")){
            log.error("create withdrawal order fail,{}",submitOrderResp.getMsg());
            throw new BizException.Init("调用Fy创建取款订单失败");
        }
        withdrawalLog.setThirdOrderId(Long.parseLong(submitOrderResp.getSysOrderID()));
        withdrawalLog.setFee(new BigDecimal(submitOrderResp.getFee()));
        withdrawalLog.setStatus(WithdrawalStatus.WAIT_PAY);
        updateById(withdrawalLog);
    }

    @Override
    @RedissonLock(value = "modifyWithdrawalStatus",key = {"#req.OrderID"})
    @Transactional
    public String modifyWithdrawalStatus(WithdrawalNotifyReq req) {
        //判断订单状态,查找待付款的订单
        WithdrawalLog withdrawalLog = queryChain().select()
                .from(WITHDRAWAL_LOG)
                .where(WITHDRAWAL_LOG.ID.eq(req.getOrderID()))
                .and(WITHDRAWAL_LOG.STATUS.eq(WithdrawalStatus.WAIT_PAY))
                .one();
        if(null==withdrawalLog){
            log.error("cant find withdrawal order :"+req.getOrderID());
            return FAILED;
        }
        //如果订单为失败状态，则回滚用户扣除的额度
        if(CharSequenceUtil.equals(req.getStatus(), FyStatus.FAILED.getCode())){
            customerWalletService.updateBalance(UpdateBalanceReq.builder()
                            .transferType(TransferType.WITHDRAWAL_RETURN)
                            .customerId(withdrawalLog.getCustomerId())
                            .transferAmount(withdrawalLog.getWithdrawalAmount().negate())//取反，表示增加额度
                            .currency(withdrawalLog.getCurrency())
                            .merchantIds(List.of(withdrawalLog.getMerchantId()))
                    .build());
            log.info("withdrawal failed,return credit");
            withdrawalLog.setStatus(WithdrawalStatus.FAILED);
            updateById(withdrawalLog);
        }
        //如果订单为待支付则回复通知为failed
        if(CharSequenceUtil.equals(req.getStatus(),FyStatus.WAIT_PAY.getCode())){
            return FAILED;
        }
        //如果订单为成功，则回复success
        if(CharSequenceUtil.equals(req.getStatus(),FyStatus.SUCCESS.getCode())){
            boolean update = updateChain()
                    .set(WITHDRAWAL_LOG.STATUS, WithdrawalStatus.SUCCESS)
                    .set(WITHDRAWAL_LOG.WITHDRAWAL_AMOUNT,new BigDecimal(req.getAmount()))
                    .where(WITHDRAWAL_LOG.ID.eq(req.getOrderID()))
                    .and(WITHDRAWAL_LOG.STATUS.eq(WithdrawalStatus.WAIT_PAY))
                    .update();
            if(update){
                return "success";
            }
        }
        return FAILED;
    }
}
