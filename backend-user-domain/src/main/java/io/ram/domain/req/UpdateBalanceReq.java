package io.ram.domain.req;


import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UpdateBalanceReq extends BaseReq{
    private Long customerId;

}
