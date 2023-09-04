package io.ram.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FyStatus {
    FAILED("0","失败"),
    SUCCESS("1","成功"),
    WAIT_PAY("2","待支付")
    ;
    private String code;
    private String desc;
}
