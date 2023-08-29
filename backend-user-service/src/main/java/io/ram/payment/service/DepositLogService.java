package io.ram.payment.service;

import com.mybatisflex.core.service.IService;
import io.ram.domain.req.deposit.DepositCreateReq;
import io.ram.domain.req.fy.DepositNotifyReq;
import io.ram.payment.entity.DepositLog;
import jakarta.validation.Valid;

/**
 * 存款表 服务层。
 *
 * @author warren
 * @since 2023-08-25
 */
public interface DepositLogService extends IService<DepositLog> {
    /**
     * 创建订单，返回url
     * @param req
     * @return
     */
    String createFromFyChannel(@Valid DepositCreateReq req);

    String fyDepositNotify(DepositNotifyReq req);
}
