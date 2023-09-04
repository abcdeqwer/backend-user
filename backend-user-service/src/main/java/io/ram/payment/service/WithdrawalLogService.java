package io.ram.payment.service;

import com.mybatisflex.core.service.IService;
import io.ram.domain.req.fy.withdrawal.WithdrawalNotifyReq;
import io.ram.domain.req.withdrawal.CreateWithdrawalReq;
import io.ram.payment.entity.WithdrawalLog;

/**
 * 取款表 服务层。
 *
 * @author warren
 * @since 2023-08-30
 */
public interface WithdrawalLogService extends IService<WithdrawalLog> {
    void createWithdrawalOrder(CreateWithdrawalReq req);

    /**
     * 改变订单状态
     * @return
     */
    String modifyWithdrawalStatus(WithdrawalNotifyReq req);
}
