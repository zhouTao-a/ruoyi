package org.dromara.mes.rec.domain.bo;

import org.dromara.mes.rec.domain.RecGoal;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;

/**
 * 目标业务对象 mes_rec_goal
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RecGoal.class, reverseConvertGenerate = false)
public class RecGoalBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 父目标
     */
    private Long parentId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 用户
     */
    private Long userId;


}
