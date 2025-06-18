package org.dromara.mes.msg.domain.vo;

import org.dromara.mes.msg.domain.MsgMatterGroup;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 事件组视图对象 mes_msg_matter_group
 *
 * @author allen
 * @date 2025-06-18
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MsgMatterGroup.class)
public class MsgMatterGroupVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 事件ID
     */
    @ExcelProperty(value = "事件ID")
    private Long matterId;

    /**
     * 分组ID
     */
    @ExcelProperty(value = "分组ID")
    private Long groupId;


}
