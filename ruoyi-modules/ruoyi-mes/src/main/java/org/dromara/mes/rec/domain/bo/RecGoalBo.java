package org.dromara.mes.rec.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.mes.rec.domain.RecGoal;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

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
     * 层级
     */
    private Integer level;

    /**
     * 顶层ID
     */
    private Long topId;

    /**
     * 顶层ID
     */
    private List<Long> topIds;

    /**
     * 父目标
     */
    private Long parentId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(max = 256, message = "标题长度不能超过255个字符")
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(max = 1024, message = "内容长度不能超过1024个字符")
    private String content;

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deadLine;

    /**
     * 截止日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDeadLine;

    /**
     * 截止日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDeadLine;

    /**
     * 排序
     */
    private Long sortOrder;

    /**
     * 用户
     */
    private Long userId;



}
