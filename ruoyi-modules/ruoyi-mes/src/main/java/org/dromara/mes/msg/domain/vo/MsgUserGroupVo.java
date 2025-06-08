package org.dromara.mes.msg.domain.vo;

import org.dromara.mes.msg.domain.MsgUserGroup;
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
 * 用户组视图对象 mes_msg_user_group
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MsgUserGroup.class)
public class MsgUserGroupVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 分组ID
     */
    @ExcelProperty(value = "分组ID")
    private Long groupId;

    /**
     * 代际差
     */
    @ExcelProperty(value = "代际差")
    private Long relativeGenerationDiff;

    /**
     * 亲缘关系（close, distant, friend, stranger）
     */
    @ExcelProperty(value = "亲缘关系", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "kinship_level")
    private String kinshipLevel;


}
