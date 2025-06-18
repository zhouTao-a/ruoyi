package org.dromara.mes.msg.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 用户对象 mes_msg_user
 *
 * @author zhout
 * @date 2025-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_msg_user")
public class MsgUser extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户编码
     */
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
    private Date birthday;

    /**
     * 农历出生日期
     */
    private Date lunarBirthday;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 短信通知
     */
    private String smsNotifyFlag;

    /**
     * 邮箱通知
     */
    private String emailNotifyFlag;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private String deletedFlag;

    /**
     * 父级ID
     */
    private Long fatherId;

    /**
     * 母亲ID
     */
    private Long motherId;

    /**
     * 配偶ID
     */
    private Long spouseId;

}
