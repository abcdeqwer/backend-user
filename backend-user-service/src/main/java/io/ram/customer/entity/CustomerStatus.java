package io.ram.customer.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户状态表 实体类。
 *
 * @author warren
 * @since 2023-08-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_customer_status")
public class CustomerStatus implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Generator, value = "flex")
    private Long id;

    private Long customerId;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 状态值
     */
    private String statusValue;

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
