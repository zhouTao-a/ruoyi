package org.dromara.mes.msg.domain.bo;

import org.dromara.mes.msg.domain.MsgUser;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户业务对象 mes_msg_user
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MsgUser.class, reverseConvertGenerate = false)
public class MsgUserBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userName;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gender;

    /**
     * 用户编码
     */
    @NotBlank(message = "用户编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userCode;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    /**
     * 农历出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lunarBirthday;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 是否接收短信通知（T/F）
     */
    private String smsNotifyFlag;

    /**
     * 是否接收邮箱通知（T/F）
     */
    private String emailNotifyFlag;


}
