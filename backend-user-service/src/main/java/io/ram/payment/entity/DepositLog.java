package io.ram.payment.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.ram.enums.Currency;
import io.ram.enums.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 存款表 实体类。
 *
 * @author warren
 * @since 2023-08-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_deposit_log")
public class DepositLog implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Generator, value = "flex")
    private Long id;

    private Long customerId;

    /**
     * 订单金额
     */
    private BigDecimal depositAmount;

    /**
     * 上分金额
     */
    private BigDecimal notifyAmount;

    /**
     * 币种
     */
    private Currency currency;

    /**
     * 状态
     */
    private DepositStatus status;

    /**
     * 通知时间
     */
    private LocalDateTime notifyTime;

    /**
     * 备注
     */
    private String remark;

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
     * 订单完成时间
     */
    private LocalDateTime completeTime;
    /**
     * 支付系统订单号
     */
    private String merchantOrderId;

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
