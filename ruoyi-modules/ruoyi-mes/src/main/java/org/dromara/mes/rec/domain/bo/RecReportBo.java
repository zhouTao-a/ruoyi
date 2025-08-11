package org.dromara.mes.rec.domain.bo;

import org.dromara.mes.rec.domain.RecReport;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;

/**
 * 报告业务对象 mes_rec_report
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RecReport.class, reverseConvertGenerate = false)
public class RecReportBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 类型
     */
    @NotBlank(message = "类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String reportType;

    /**
     * 日期
     */
    @NotNull(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date reportDate;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 用户
     */
    private Long userId;


}
