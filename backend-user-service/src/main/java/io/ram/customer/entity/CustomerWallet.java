package io.ram.customer.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.ram.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户钱包表 实体类。
 *
 * @author warren
 * @since 2023-08-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_customer_wallet")
public class CustomerWallet implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Generator, value = "flex")
    private Long id;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 货币类型
     */
    private Currency currency;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 商户id
     */
    private String merchantId;

    /**
     * 乐观锁
     */
    @Column(version = true)
    private Integer revision;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "now()")
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    @Column(onUpdateValue = "now()")
    private LocalDateTime updatedTime;

}
