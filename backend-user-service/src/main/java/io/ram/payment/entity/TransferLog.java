package io.ram.payment.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.ram.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 额度记录变更表 实体类。
 *
 * @author warren
 * @since 2023-08-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_transfer_log")
public class TransferLog implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Generator, value = "flex")
    private Long id;

    private Long customerId;

    /**
     * 帐变前余额
     */
    private BigDecimal beforeAmount;

    /**
     * 帐变后余额
     */
    private BigDecimal afterAmount;

    /**
     * 帐变额度
     */
    private BigDecimal amount;

    /**
     * 帐变类型
     */
    private TransferType transferType;

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
