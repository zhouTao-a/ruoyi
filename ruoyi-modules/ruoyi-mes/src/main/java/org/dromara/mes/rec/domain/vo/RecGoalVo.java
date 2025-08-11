package org.dromara.mes.rec.domain.vo;

import java.util.Date;

import org.dromara.mes.rec.domain.RecGoal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 目标视图对象 mes_rec_goal
 *
 * @author allen
 * @date 2025-08-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = RecGoal.class)
public class RecGoalVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 父目标
     */
    @ExcelProperty(value = "父目标")
    private Long parentId;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;

    /**
     * 进度
     */
    @ExcelProperty(value = "进度")
    private Long progress;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 截止日期
     */
    @ExcelProperty(value = "截止日期")
    private Date deadLine;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sortOrder;

    /**
     * 用户
     */
    private Long userId;


}
