package org.dromara.mes.msg.domain.vo;

import org.dromara.mes.msg.domain.MsgUserGroup;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


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
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名称")
    private String userName;

    /**
     * 用户编码
     */
    @ExcelProperty(value = "用户编码")
    private String userCode;

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
     * 辈分差
     */
    private Long relativeGenerationDiff;

    /**
     * 亲缘关系（spouse, close, distant, friend, stranger）
     */
    private String kinshipLevel;
}
