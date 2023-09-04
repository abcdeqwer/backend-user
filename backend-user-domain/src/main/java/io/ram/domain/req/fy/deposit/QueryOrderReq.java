package io.ram.domain.req.fy.deposit;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class QueryOrderReq {
    private String MerchantID;
    private String OrderID;
    private String Sign;

    public void doSign(String key){
        Map<String, Object> req= BeanUtil.beanToMap(this, false, true);
        req = MapUtil.sort(req);
        var reqStr= req.toString().replaceAll(",","&").replaceAll(" ","");
        reqStr= reqStr.substring(1, reqStr.length() - 1);
        reqStr = reqStr+key;
        this.setSign(DigestUtil.md5Hex(reqStr).toUpperCase());

    }

}
