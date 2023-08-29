package io.ram.domain.req.fy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
@NoArgsConstructor
@AllArgsConstructor
public class DepositNotifyReq {
    private String MerchantID;
    private String OrderID;
    private String SysOrderID	;
    private String CompleteTime	;
    private String Amount	;
    private String Status	;
    private String Remarks;
    private String Sign;
    private String Code;
    private String Msg;
}
