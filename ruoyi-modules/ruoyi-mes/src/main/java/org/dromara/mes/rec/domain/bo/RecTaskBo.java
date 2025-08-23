package org.dromara.mes.rec.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.mes.rec.domain.RecTask;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 任务业务对象 mes_rec_task
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RecTask.class, reverseConvertGenerate = false)
public class RecTaskBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(max = 255, message = "标题长度不能超过255个字符")
    private String title;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 进度
     */
    private Long progress;

    /**
     * 截止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deadLine;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 截止日期查询
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDeadLine;

    /**
     * 截止日期查询
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDeadLine;


}
