package org.dromara.mes.rec.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

import java.io.Serial;

/**
 * 目标对象 mes_rec_goal
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_rec_goal")
public class RecGoal extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 父目标
     */
    private Long parentId;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 进度
     */
    private Long progress;

    /**
     * 状态
     */
    private String status;

    /**
     * 截止日期
     */
    private Date deadLine;

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
