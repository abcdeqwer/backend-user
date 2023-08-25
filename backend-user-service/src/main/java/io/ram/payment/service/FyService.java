package io.ram.payment.service;

import com.dtflys.forest.annotation.Address;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import io.ram.domain.req.fy.SubmitOrderReq;
import io.ram.domain.resp.fy.SubmitOrderResp;

@Address(host = "#{fy.domain}",port = "80")
public interface FyService {

    @Post(url="/api/Bxds/SubmitOrder")
    SubmitOrderResp submitOrder(@JSONBody SubmitOrderReq submitOrderReq);

}
