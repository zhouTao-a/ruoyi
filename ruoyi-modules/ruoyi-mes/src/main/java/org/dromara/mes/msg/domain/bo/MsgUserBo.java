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
    @Size(min = 0, max = 20, message = "用户名长度不能超过20个字符", groups = { AddGroup.class, EditGroup.class })
    private String userName;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(max = 2, message = "性别长度不能超过2个字符", groups = { AddGroup.class, EditGroup.class })
    private String gender;

    /**
     * 用户编码
     */
    @NotBlank(message = "用户编码不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(max = 20, message = "用户编码长度不能超过20个字符", groups = { AddGroup.class, EditGroup.class })
    private String userCode;

    /**
     * 身份证号
     */
    @Size(max = 18, message = "身份证号长度不能超过18个字符", groups = { AddGroup.class, EditGroup.class })
    private String idCard;

    /**
     * 手机号
     */
    @Size(max = 11, message = "手机号长度不能超过11个字符", groups = { AddGroup.class, EditGroup.class })
    private String phoneNumber;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthday;

    /**
     * 农历出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lunarBirthday;

    /**
     * 邮箱地址
     */
    @Size(max = 50, message = "邮箱地址长度不能超过50个字符", groups = { AddGroup.class, EditGroup.class })
    @Email(message = "邮箱格式不正确", groups = { AddGroup.class, EditGroup.class })
    private String email;

    /**
     * 短信通知
     */
    private String smsNotifyFlag;

    /**
     * 邮箱通知
     */
    private String emailNotifyFlag;


}
