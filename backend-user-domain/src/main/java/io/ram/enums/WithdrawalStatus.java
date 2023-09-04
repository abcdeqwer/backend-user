package io.ram.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WithdrawalStatus {
    INIT("init","初始化"),
    WAIT_PAY("waitPay","待付款"),
    SUCCESS("success","付款成功"),
    FAILED("failed","明确失败")
    ;
    @EnumValue
    private String code;
    private String desc;
}
