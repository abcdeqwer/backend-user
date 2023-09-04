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
public class SubmitDepositOrderReq {
    private String MerchantID;
    private String OrderID;
    /**
     * yyyyMMddHHmmss
     */
    private String Date;
    private String NotifyUrl;
    private String CallBackUrl;
    private String Amount;
    private String MerchantNumber;
    private String UserName;
    private String Sign;

    public void doSign(String key){
        Map<String, Object> req= BeanUtil.beanToMap(this, false, true);
        req = MapUtil.sort(req);
        req.remove("UserName");
        var reqStr= req.toString().replaceAll(",","&").replaceAll(" ","");
        reqStr= reqStr.substring(1, reqStr.length() - 1);
        reqStr = reqStr+key;
        this.setSign(DigestUtil.md5Hex(reqStr).toUpperCase());

    }
    public static void main(String[] args) {
        SubmitDepositOrderReq test = SubmitDepositOrderReq.builder()
                .MerchantID("916840")
                .OrderID("2020021002411")
                .Date("202002100241")
                .NotifyUrl("http://baidu.com")
                .CallBackUrl("http://baidu.com")
                .Amount("50")
                .MerchantNumber("1001")
                .UserName("kevin")
                .build();
        test.doSign("211D1356259FA6329AD826A131481647");
        System.out.println(test.getSign());
    }
}
