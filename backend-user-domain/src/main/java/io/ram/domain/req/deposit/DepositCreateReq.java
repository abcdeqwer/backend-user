package io.ram.domain.req.deposit;

import io.ram.domain.req.BaseReq;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DepositCreateReq extends BaseReq {
    @NotNull(message = "customer.id.not.null")
    private Long customerId;

    @NotNull(message = "amount.not.null")
    @Min(value = 0,message = "amount.must.bigger.then.0")
    private BigDecimal amount;

}
