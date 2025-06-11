package org.dromara.mes.msg.domain.vo;

import org.dromara.mes.msg.domain.MsgGroup;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 分组信息视图对象 mes_msg_group
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MsgGroup.class)
public class MsgGroupVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 分组名称
     */
    @ExcelProperty(value = "分组名称")
    private String groupName;

    /**
     * 分组编码
     */
    @ExcelProperty(value = "分组编码")
    private String groupCode;

    /**
     * 默认参考用户ID
     */
    @ExcelProperty(value = "默认参考用户ID")
    private Long defaultTargetUserId;


}
