package org.dromara.mes.rec.domain.vo;

import java.util.Date;

import org.dromara.mes.rec.domain.RecTask;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 任务视图对象 mes_rec_task
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = RecTask.class)
public class RecTaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String content;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "task_status")
    private String status;

    /**
     * 进度
     */
    @ExcelProperty(value = "进度")
    private Long progress;

    /**
     * 截止日期
     */
    @ExcelProperty(value = "截止日期")
    private Date deadLine;

    /**
     * 优先级
     */
    @ExcelProperty(value = "优先级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "priority")
    private String priority;

    /**
     * 用户
     */
    private Long userId;


}
