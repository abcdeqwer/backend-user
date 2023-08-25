package io.ram.domain.req;


import io.ram.enums.Currency;
import io.ram.enums.TransferType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBalanceReq extends BaseReq {
    /**
     * 用户id
     */
    @NotNull(message = "customerId.not.null")
    private Long customerId;
    /**
     * 交易类型
     */
    @NotNull(message = "transferType.not.null")
    private TransferType transferType;
    /**
     * 交易额度
     */
    @NotNull(message = "transferAmount.not.null")
    private BigDecimal transferAmount;
    /**
     * 上分币种
     */
    @NotNull(message = "currency.not.null")
    private Currency currency;
}
