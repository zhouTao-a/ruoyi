package org.dromara.mes.rec.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 自省主题 mes_rec_introspect。同一用户同时只能有一条 status=active。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_rec_introspect")
public class RecIntrospect extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 主题标题，如「控制情绪」
     */
    private String title;

    /**
     * 是否生效：active / inactive
     */
    private String status;

    /**
     * 排序
     */
    private Long sortOrder;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private String deletedFlag;
}
