package io.ram.payment.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import io.ram.config.DateUtil;
import io.ram.config.redisson.RedissonLock;
import io.ram.customer.service.CustomerWalletService;
import io.ram.domain.req.UpdateBalanceReq;
import io.ram.domain.req.deposit.DepositCreateReq;
import io.ram.domain.req.fy.DepositNotifyReq;
import io.ram.domain.req.fy.SubmitOrderReq;
import io.ram.domain.resp.fy.SubmitOrderResp;
import io.ram.enums.Currency;
import io.ram.enums.DepositStatus;
import io.ram.enums.TransferType;
import io.ram.exception.BizException;
import io.ram.payment.entity.DepositLog;
import io.ram.payment.mapper.DepositLogMapper;
import io.ram.payment.service.DepositLogService;
import io.ram.payment.service.FyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.ZoneId;

import static io.ram.payment.entity.table.DepositLogTableDef.DEPOSIT_LOG;

/**
 * 存款表 服务层实现。
 *
 * @author warren
 * @since 2023-08-25
 */
@Service
@Validated
@Slf4j
public class DepositLogServiceImpl extends ServiceImpl<DepositLogMapper, DepositLog> implements DepositLogService {
    @Autowired
    private FyService fyService;
    @Value("${fy.token}")
    private String fyToken;
    @Value("${fy.merchantId}")
    private String merchantId;
    @Value("${fy.deposit.notifyUrl}")
    private String depositNotifyUrl;
    @Autowired
    private CustomerWalletService customerWalletService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createFromFyChannel(DepositCreateReq req) {
        var date = DateUtil.getNowByUTC();
        DepositLog depositLog = DepositLog.builder()
                .depositAmount(req.getAmount())
                .merchantId(req.getMerchantIds().get(0))
                .customerId(req.getCustomerId())
                .currency(Currency.BRL)
                .status(DepositStatus.INIT)
                .build();
        this.save(depositLog);
        SubmitOrderReq fyReq = SubmitOrderReq.builder()
                .MerchantID(merchantId)
                .OrderID(depositLog.getId() + "")
                .Date(DateUtil.formatToyyyyMMddHHmmss(date))
                .NotifyUrl(depositNotifyUrl)
                .CallBackUrl(depositNotifyUrl)
                .Amount(req.getAmount() + "")
                //存款固定渠道1005
                .MerchantNumber("1005")
                .UserName(req.getRealName())
                .build();
        fyReq.doSign(fyToken);
        SubmitOrderResp submitOrderResp = fyService.submitOrder(fyReq);
        log.info("call fy response:{}", JSONUtil.toJsonStr(submitOrderResp));
        if (StrUtil.equalsIgnoreCase(submitOrderResp.getCode(), "success")) {
            //更新订单状态为待支付
            depositLog.setStatus(DepositStatus.WAIT_PAY);
            this.updateById(depositLog);
            return submitOrderResp.getUrl();
        } else {
            throw new BizException.Init("error call fy service," + JSONUtil.toJsonStr(submitOrderResp));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(value = "depositNotify", key = {"#req.OrderID"})
    public String fyDepositNotify(DepositNotifyReq req) {
        //如果订单为失败状态
        if (StrUtil.equals(req.getStatus(), "0")) {
            updateChain().of(DEPOSIT_LOG)
                    .set(DEPOSIT_LOG.STATUS, DepositStatus.FAILED)
                    .where(DEPOSIT_LOG.ID.eq(req.getSysOrderID()))
                    .update();
            return "success";
        }
        //如果订单为成功状态
        if (StrUtil.equals(req.getStatus(), "1")) {
            DepositLog depositLog = queryChain().select()
                    .from(DEPOSIT_LOG)
                    .where(DEPOSIT_LOG.ID.eq(req.getOrderID()))
                    .and(DEPOSIT_LOG.STATUS.eq(DepositStatus.WAIT_PAY)).one();
            //找不到待支付订单 直接返回失败
            if (null == depositLog) {
                log.warn("can't find deposit log id:{} status:wait_pay ", req.getOrderID());
                return "fail";
            }
            //更新用户余额
            UpdateBalanceReq updateBalanceReq = UpdateBalanceReq.builder()
                    .transferType(TransferType.DEPOSIT)
                    .customerId(depositLog.getCustomerId())
                    .currency(Currency.BRL)
                    .merchantIds(ListUtil.of(depositLog.getMerchantId()))
                    .transferAmount(new BigDecimal(req.getAmount()))
                    .build();
            boolean b = customerWalletService.updateBalance(updateBalanceReq);
            if (!b) {
                log.error("update customer balance failed");
                //更新失败
                return "fail";
            }
            depositLog.setNotifyAmount(new BigDecimal(req.getAmount()));
            depositLog.setStatus(DepositStatus.SUCCESS);
            depositLog.setCompleteTime(DateUtil.parseFromyyyyMMddHHmmssToUtc(req.getCompleteTime(), ZoneId.of("+8")));
            updateById(depositLog);
        }
        return "success";
    }
}
