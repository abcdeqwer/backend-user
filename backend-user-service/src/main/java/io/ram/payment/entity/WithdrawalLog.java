package io.ram.payment.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.ram.enums.Currency;
import io.ram.enums.IdCardType;
import io.ram.enums.WithdrawalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 取款表 实体类。
 *
 * @author warren
 * @since 2023-08-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_withdrawal_log")
public class WithdrawalLog implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Generator, value = "flex")
    private Long id;

    private Long customerId;

    /**
     * 取款额度
     */
    private BigDecimal withdrawalAmount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 取款币种
     */
    private Currency currency;

    /**
     * 状态
     */
    private WithdrawalStatus status;

    /**
     * 三方支付订单号
     */
    private Long thirdOrderId;

    /**
     * 证件号 CPF 或 CNPJ
     */
    private String cardId;

    /**
     * 1 cpf 2 email 3 phone(需加巴西区号 例如+55) 4 EVP 5 CNPJ
     */
    private IdCardType type;

    /**
     * 传对应模式账号
     */
    private String account;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 商户id
     */
    private String merchantId;

    /**
     * 乐观锁
     */
    private Integer revision;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

}
