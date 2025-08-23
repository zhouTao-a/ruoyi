package org.dromara.mes.rec.domain.vo;

import java.util.Date;

import org.dromara.mes.rec.domain.RecReport;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 报告视图对象 mes_rec_report
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = RecReport.class)
public class RecReportVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String reportType;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private Date reportDate;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    private String content;

    /**
     * 摘要
     */
    @ExcelProperty(value = "摘要")
    private String summary;

    /**
     * 用户
     */
    private Long userId;


}
