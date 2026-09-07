package org.dromara.mes.rec.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.Date;

/**
 * 自省按天明细。同一主题、同一天可以有多条情况说明。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_rec_introspect_item")
public class RecIntrospectItem extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 所属主题
     */
    private Long introspectId;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 发生日期
     */
    private Date occurDate;

    /**
     * 情况说明
     */
    private String content;

    /**
     * 当天内排序
     */
    private Long sortOrder;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private String deletedFlag;
}
