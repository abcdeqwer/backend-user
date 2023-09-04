package io.ram.domain.resp.fy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitOrderResp {
    private String Code;
    private String Msg;
    private String Url;
    private String SysOrderID;
    private String OrderID;
    private String Amount;
    private String Fee;
    private String Status;
}
