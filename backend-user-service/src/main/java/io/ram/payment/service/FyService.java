package io.ram.payment.service;

import com.dtflys.forest.annotation.Address;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import io.ram.domain.req.fy.deposit.DepositNotifyReq;
import io.ram.domain.req.fy.deposit.QueryOrderReq;
import io.ram.domain.req.fy.deposit.SubmitDepositOrderReq;
import io.ram.domain.req.fy.withdrawal.SubmitWithdrawalOrderReq;
import io.ram.domain.resp.fy.SubmitOrderResp;

@Address(basePath = "#{fy.domain}")
public interface FyService {

    @Post(url="/api/Bxds/SubmitOrder")
    SubmitOrderResp submitOrder(@JSONBody SubmitDepositOrderReq submitDepositOrderReq);

    /**
     * 查询订单状态
     * @param req
     * @return
     */
    @Post(url="/api/Bx/QueryOrder")
    DepositNotifyReq queryOrder(@JSONBody QueryOrderReq req);

    @Post(url = "/api/Bx/SubmitOrder")
    SubmitOrderResp submitWithdrawOrder(@JSONBody SubmitWithdrawalOrderReq req);

}
