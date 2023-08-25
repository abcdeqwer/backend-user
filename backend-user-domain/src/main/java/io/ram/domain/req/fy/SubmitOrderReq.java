package io.ram.domain.req.fy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SubmitOrderReq {
    private String MerchantID;
    private String OrderId;
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
        var reqStr= req.toString().replaceAll(",","&").replaceAll(" ","");
        reqStr= reqStr.substring(1, reqStr.length() - 1);
        reqStr = reqStr+key;
        this.setSign(MD5.create().digestHex(reqStr).toUpperCase());
    }

    public static void main(String[] args) {
        SubmitOrderReq test = SubmitOrderReq.builder()
                .MerchantID("80")
                .OrderId("2000")
                .Date("20211111111100")
                .NotifyUrl("http://www.baidu.com")
                .CallBackUrl("http://www.baidu.com")
                .Amount("50")
                .MerchantNumber("1005")
                .UserName("test")
                .build();
        test.doSign("testk");
        System.out.println(test.getSign());
    }
}
