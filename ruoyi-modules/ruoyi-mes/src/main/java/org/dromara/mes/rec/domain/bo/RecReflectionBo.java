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
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源名称
     */
    private String sourceName;

    /**
     * 来源链接
     */
    private String sourceLink;

    /**
     * 用户
     */
    private Long userId;


}
