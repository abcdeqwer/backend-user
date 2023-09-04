package io.ram.payment.task;

import cn.hutool.core.text.CharSequenceUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.ram.config.DateUtil;
import io.ram.config.redisson.RedissonLock;
import io.ram.domain.req.fy.deposit.QueryOrderReq;
import io.ram.enums.DepositStatus;
import io.ram.payment.entity.DepositLog;
import io.ram.payment.service.DepositLogService;
import io.ram.payment.service.FyService;
import io.ram.payment.service.WithdrawalLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.ram.payment.entity.table.DepositLogTableDef.DEPOSIT_LOG;

@Component
@Slf4j
public class FyPaymentTask {
    @Autowired
    private DepositLogService depositLogService;
    @Autowired
    private WithdrawalLogService withdrawalLogService;
    @Autowired
    private FyService fyService;

    @Value("${fy.token}")
    private String fyToken;
    @Value("${fy.merchantId}")
    private String merchantId;

    @RedissonLock(value = "checkDepositStatusJob")
    @XxlJob("checkDepositStatus")
    public void checkDeposit() {
        log.info("starting check wait_pay order status");
        List<DepositLog> list = depositLogService.queryChain().select(DEPOSIT_LOG.ID)
                .from(DEPOSIT_LOG)
                .where(DEPOSIT_LOG.STATUS.eq(DepositStatus.WAIT_PAY))
                .and(DEPOSIT_LOG.CREATED_TIME.ge(DateUtil.getNowByUTC().minusHours(6))).list();
        for (DepositLog depositLog : list) {
            QueryOrderReq req= QueryOrderReq.builder()
                    .MerchantID(merchantId)
                    .OrderID(depositLog.getId()+"")
                    .build();
            req.doSign(fyToken);
            var queryOrderResp = fyService.queryOrder(req);
            if(CharSequenceUtil.equalsIgnoreCase(queryOrderResp.getCode(),"success")){
                depositLogService.fyDepositNotify(queryOrderResp);
            }
        }
        log.info("ending check wait_pay order status");
    }
    @RedissonLock(value = "checkWithdrawalStatusJob")
    @XxlJob("checkWithdrawalStatus")
    public void checkWithdrawal(){
        log.info("starting check wait_pay withdraw order status");
        log.info("ending check wait_pay withdraw order status");
    }
}
