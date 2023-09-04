package io.ram.payment.controller.withdrawal;

import io.ram.domain.R;
import io.ram.domain.req.fy.withdrawal.WithdrawalNotifyReq;
import io.ram.domain.req.withdrawal.CreateWithdrawalReq;
import io.ram.payment.service.WithdrawalLogService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * api/取款订单
 */
@RestController("/api/payment/withdrawal")
@Slf4j
public class WithdrawalApi {
    @Autowired
    private WithdrawalLogService withdrawalLogService;

    @PostMapping("/createWithdrawal")
    public R createWithdrawal(@RequestBody @Valid CreateWithdrawalReq req) {
        withdrawalLogService.createWithdrawalOrder(req);
        return R.success();
    }

    /**
     * 取款订单回调通知
     * @param req
     * @return
     */
    @PostMapping("/notifyWithdrawal")
    public R<String> notifyWithdrawal(@RequestBody WithdrawalNotifyReq req) {
        return R.success(withdrawalLogService.modifyWithdrawalStatus(req));
    }
}
