package io.ram.domain.req.withdrawal;

import io.ram.domain.req.BaseReq;
import io.ram.enums.Currency;
import io.ram.enums.IdCardType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWithdrawalReq extends BaseReq {
    /**
     * 客户id
     */
    @NotNull(message = "customerId.not.null")
    private Long customerId;

    /**
     * 提款额度
     */
    @NotNull(message = "amount.not.null")
    @Min(value = 0L,message = "amount.must.bigger.then.0")
    private BigDecimal amount;

    /**
     * 提款币种
     */
    private Currency currency;
    /**
     * 证件号,CPF or CNPJ
     */
    @NotEmpty(message = "cardId.not.null")
    private String cardId;
    /**
     * 证件模式
     */
    @NotNull(message = "type.not.null")
    private IdCardType type;
    /**
     * 对应模式的账号
     */
    @NotEmpty(message = "account.not.null")
    private String account;

}
