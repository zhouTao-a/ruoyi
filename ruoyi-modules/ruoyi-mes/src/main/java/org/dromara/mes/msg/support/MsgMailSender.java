package org.dromara.mes.msg.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.enums.FormatsType;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mail.utils.MailUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.service.IRecReflectionService;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 事件提醒邮件发送。当前固定发给配置的接收邮箱，后续再按用户开放。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MsgMailSender {

    private final IRecReflectionService recReflectionService;
    private final ISysUserService sysUserService;

    @Value("${msg.notify.mail-to:}")
    private String mailTo;

    @Value("${msg.notify.rec-url:http://www.allen-z.cn/rec-total}")
    private String recUrl;

    @Value("${msg.notify.reflection-username:allen}")
    private String reflectionUsername;

    /**
     * @return 发送成功返回 true，未配置或失败返回 false（调用方据此决定是否滚动下次时间）
     */
    public boolean sendDayMatter(MsgDayMatterVo item) {
        if (StringUtils.isBlank(mailTo)) {
            log.warn("未配置 msg.notify.mail-to，跳过邮件发送，事件={}", item.getDayName());
            return false;
        }
        try {
            String subject = "涵涵通知：" + item.getDayName();
            String content = buildContent(item);
            MailUtils.sendText(mailTo, subject, content);
            log.info("事件邮件已发送 to={}, 事件={}", mailTo, item.getDayName());
            return true;
        } catch (Exception e) {
            log.error("事件邮件发送失败，事件={}", item.getDayName(), e);
            return false;
        }
    }

    /**
     * 正文顺序：事件 → 感想 → 访问地址
     */
    String buildContent(MsgDayMatterVo item) {
        String time = item.getNextNotifyTime() == null ? ""
            : DateUtils.parseDateToStr(FormatsType.YYYY_MM_DD_HH_MM, item.getNextNotifyTime());
        return "【事件】\n"
            + "事件名称：" + nullToEmpty(item.getDayName()) + "\n"
            + "事件类型：" + typeLabel(item.getDayType()) + "\n"
            + "通知时间：" + time + "\n"
            + "\n"
            + buildReflectionBlock()
            + "\n"
            + "访问地址：" + recUrl;
    }

    private String buildReflectionBlock() {
        RecReflectionVo reflection = pickReflection();
        if (reflection == null) {
            return "【感想】\n暂无感想\n";
        }
        StringBuilder sb = new StringBuilder("【感想】\n");
        sb.append("标题：").append(nullToEmpty(reflection.getTitle())).append('\n');
        if (StringUtils.isNotBlank(reflection.getSynopsis())) {
            sb.append("概要：").append(reflection.getSynopsis()).append('\n');
        }
        sb.append("感想：").append(nullToEmpty(reflection.getContent())).append('\n');
        if (StringUtils.isNotBlank(reflection.getSourceName())) {
            sb.append("来源：").append(reflection.getSourceName()).append('\n');
        }
        return sb.toString();
    }

    /**
     * 固定用配置的系统用户（默认 allen）抽一条感想，不按事件 userId。
     */
    private RecReflectionVo pickReflection() {
        Long userId = resolveReflectionUserId();
        if (userId == null) {
            return null;
        }
        try {
            return TenantHelper.ignore(() -> recReflectionService.pickRandom(userId));
        } catch (Exception e) {
            log.warn("抽取感想失败，邮件仍发送事件与链接", e);
            return null;
        }
    }

    private Long resolveReflectionUserId() {
        if (StringUtils.isBlank(reflectionUsername)) {
            return null;
        }
        try {
            SysUserVo user = TenantHelper.ignore(() -> sysUserService.selectUserByUserName(reflectionUsername));
            if (user == null || user.getUserId() == null) {
                log.warn("未找到感想用户 username={}", reflectionUsername);
                return null;
            }
            return user.getUserId();
        } catch (Exception e) {
            log.warn("查询感想用户失败 username={}", reflectionUsername, e);
            return null;
        }
    }

    private String typeLabel(String dayType) {
        if (dayType == null) {
            return "";
        }
        return switch (dayType) {
            case "birthday" -> "生日";
            case "anniversary" -> "纪念日";
            case "life" -> "生活";
            case "work" -> "工作";
            default -> dayType;
        };
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
