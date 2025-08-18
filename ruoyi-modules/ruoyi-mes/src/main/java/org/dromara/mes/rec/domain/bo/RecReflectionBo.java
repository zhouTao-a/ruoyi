package org.dromara.mes.rec.domain.bo;

import org.dromara.mes.rec.domain.RecReflection;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 感想业务对象 mes_rec_reflection
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RecReflection.class, reverseConvertGenerate = false)
public class RecReflectionBo extends BaseEntity {

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
     * 概要
     */
    @Size(max = 20000, message = "概要长度不能超过1024个字符")
    private String synopsis;

    /**
     * 感想
     */
    @NotBlank(message = "感想不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(max = 20000, message = "感想长度不能超过20000个字符")
    private String content;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源名称
     */
    @Size(max = 255, message = "来源名称长度不能超过255个字符")
    private String sourceName;

    /**
     * 来源链接
     */
    @Size(max = 255, message = "来源链接长度不能超过255个字符")
    private String sourceLink;

    /**
     * 用户
     */
    private Long userId;


}
