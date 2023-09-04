package io.ram.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdCardType {
    CPF("1", "cpf"),
    EMAIL("2", "email"),
    PHONE("3", "phone"),
    EVP("4", "EVP"),
    CNPJ("5", "CNPJ")
    ;



    @EnumValue
    private String code;
    private String desc;
}
