package io.ram.payment.service;

import com.dtflys.forest.annotation.Address;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import io.ram.domain.req.fy.DepositNotifyReq;
import io.ram.domain.req.fy.QueryOrderReq;
import io.ram.domain.req.fy.SubmitOrderReq;
import io.ram.domain.resp.fy.SubmitOrderResp;

@Address(basePath = "#{fy.domain}")
public interface FyService {

    @Post(url="/api/Bxds/SubmitOrder")
    SubmitOrderResp submitOrder(@JSONBody SubmitOrderReq submitOrderReq);

    /**
     * 查询订单状态
     * @param req
     * @return
     */
    @Post(url="/api/Bx/QueryOrder")
    DepositNotifyReq queryOrder(@JSONBody QueryOrderReq req);


}
