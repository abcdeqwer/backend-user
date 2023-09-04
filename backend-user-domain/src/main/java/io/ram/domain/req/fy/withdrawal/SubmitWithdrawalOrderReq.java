package io.ram.domain.req.fy.withdrawal;

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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class SubmitWithdrawalOrderReq {
    private String MerchantID;
    private String OrderID;
    /**
     * yyyyMMddHHmmss
     */
    private String Date;
    private String NotifyUrl;
    private String Amount;
    private String MerchantNumber;
    /**
     * 证件号 CPF 或 CNPJ
     */
    private String Id;
    /**
     * 1 cpf 2 email 3 phone(需加巴西区号 例如+55) 4 EVP 5 CNPJ
     */
    private String Type;
    /**
     * 传对应模式账号
     */
    private String Account;
    private String Sign;

    public void doSign(String key){
        Map<String, Object> req= BeanUtil.beanToMap(this, false, true);
        req = MapUtil.sort(req);
        req.remove("Id");
        req.remove("Type");
        req.remove("Account");
        var reqStr= req.toString().replaceAll(",","&").replaceAll(" ","");
        reqStr= reqStr.substring(1, reqStr.length() - 1);
        reqStr = reqStr+key;
        this.setSign(DigestUtil.md5Hex(reqStr).toUpperCase());

    }
}
