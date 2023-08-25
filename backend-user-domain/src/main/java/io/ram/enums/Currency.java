package io.ram.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
    USDT("usdt","usdt"),
    BRL("BRL","巴西雷亚尔")
    ;
    @EnumValue
    private String code;
    private String desc;
}
