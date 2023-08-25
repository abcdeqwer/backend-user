package io.ram.domain.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageReq extends BaseReq {
    @Min(1)
    private int pageNum = 1;
    @Min(1)
    @Max(1000)
    private int pageSize = 20;
}
