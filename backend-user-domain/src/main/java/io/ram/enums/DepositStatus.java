package io.ram.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DepositStatus {
    INIT("init","初始化"),
    WAIT_PAY("waitPay","待付款"),
    SUCCESS("success","成功上分")
    ;
    @EnumValue
    private String code;
    private String desc;
}
