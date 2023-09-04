package io.ram.payment.controller.deposit;

import io.ram.config.redisson.RedissonLock;
import io.ram.domain.R;
import io.ram.domain.req.deposit.DepositCreateReq;
import io.ram.domain.req.fy.deposit.DepositNotifyReq;
import io.ram.payment.service.DepositLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 存款api
 */
@RestController("/api/payment/deposit")
@Slf4j
public class DepositApi {
    @Autowired
    private DepositLogService depositLogService;

    /**
     * 创建存款订单,返回Url
     */
    @RedissonLock(value = "createDeposit",key = {"#req.customerId"})
    @PostMapping("/createDeposit")
    public R<String> createOrder(@Validated@RequestBody DepositCreateReq req){
        return R.success(depositLogService.createFromFyChannel(req));
    }


    @PostMapping("/notifyDeposit")
    public String depositNotify(@RequestBody DepositNotifyReq req){
        return depositLogService.fyDepositNotify(req);
    }
}
