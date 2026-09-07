package org.dromara.mes.rec.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.enums.FormatsType;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mail.utils.MailUtils;
import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.domain.vo.RecIntrospectDigestVo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 每日任务/目标/自省摘要邮件。收件人由调用方传入（系统用户邮箱）。
 * 早晚报共用早报文案。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RecDigestMailSender {

    @Value("${msg.notify.rec-url:http://www.allen-z.cn/rec-total}")
    private String recUrl;

    /**
     * @param mailTo 收件人邮箱
     * @return 发送成功返回 true
     */
    public boolean sendDigest(List<RecTaskVo> tasks, List<RecGoalVo> goals, RecIntrospectDigestVo introspect,
                              RecReflectionVo reflection, String mailTo) {
        if (StringUtils.isBlank(mailTo)) {
            log.warn("未指定收件人，跳过日报邮件");
            return false;
        }
        try {
            String content = buildContent(tasks, goals, introspect, reflection);
            MailUtils.sendText(mailTo, "涵涵通知：今日任务与目标", content);
            log.info("日报邮件已发送 to={}", mailTo);
            return true;
        } catch (Exception e) {
            log.error("日报邮件发送失败 to={}", mailTo, e);
            return false;
        }
    }

    /**
     * 正文顺序：任务 → 目标 → 自省 → 感想 → 访问地址
     */
    public String buildContent(List<RecTaskVo> tasks, List<RecGoalVo> goals, RecIntrospectDigestVo introspect,
                               RecReflectionVo reflection) {
        return buildTaskBlock(tasks)
            + "\n"
            + buildGoalBlock(goals)
            + "\n"
            + buildIntrospectBlock(introspect)
            + "\n"
            + buildReflectionBlock(reflection)
            + "\n"
            + "访问地址：" + recUrl;
    }

    /**
     * 兼容旧调用：不含自省段落时按空态渲染。
     */
    public String buildContent(List<RecTaskVo> tasks, List<RecGoalVo> goals, RecReflectionVo reflection) {
        return buildContent(tasks, goals, null, reflection);
    }

    public boolean sendDigest(List<RecTaskVo> tasks, List<RecGoalVo> goals, RecReflectionVo reflection, String mailTo) {
        return sendDigest(tasks, goals, null, reflection, mailTo);
    }

    private String buildTaskBlock(List<RecTaskVo> tasks) {
        StringBuilder sb = new StringBuilder("【任务】\n");
        if (tasks == null || tasks.isEmpty()) {
            return sb.append("暂无任务\n").toString();
        }
        for (RecTaskVo task : tasks) {
            sb.append("- ").append(nullToEmpty(task.getTitle()))
                .append("（").append(formatDeadline(task.getDeadLine()))
                .append(" / ").append(statusLabel(task.getStatus()))
                .append("）\n");
            if (StringUtils.isNotBlank(task.getContent())) {
                sb.append("  描述：").append(task.getContent()).append('\n');
            }
        }
        return sb.toString();
    }

    private String buildGoalBlock(List<RecGoalVo> goals) {
        StringBuilder sb = new StringBuilder("【目标】\n");
        if (goals == null || goals.isEmpty()) {
            return sb.append("暂无目标\n").toString();
        }
        for (RecGoalVo goal : goals) {
            sb.append("- ").append(nullToEmpty(goal.getTitle()))
                .append("（").append(formatDeadline(goal.getDeadLine()))
                .append(" / ").append(statusLabel(goal.getStatus()))
                .append("）\n");
            if (StringUtils.isNotBlank(goal.getContent())) {
                sb.append("  内容：").append(goal.getContent()).append('\n');
            }
        }
        return sb.toString();
    }

    /**
     * 早晚报共用早报文案：今日生效主题 + 昨日明细回顾。
     */
    private String buildIntrospectBlock(RecIntrospectDigestVo introspect) {
        StringBuilder sb = new StringBuilder("【自省】\n");
        if (introspect == null || StringUtils.isBlank(introspect.getTodayTitle())) {
            sb.append("今日暂无自省主题\n");
        } else {
            sb.append("今日自省：").append(introspect.getTodayTitle()).append('\n');
        }
        sb.append('\n');
        List<String> yesterdayItems = introspect == null ? List.of() : introspect.getYesterdayItems();
        if (yesterdayItems == null || yesterdayItems.isEmpty()) {
            return sb.append("昨日暂无记录\n").toString();
        }
        sb.append("昨日自省：").append(nullToEmpty(introspect.getYesterdayTitle())).append('\n');
        sb.append("昨日记录 ").append(yesterdayItems.size()).append(" 次：\n");
        for (int i = 0; i < yesterdayItems.size(); i++) {
            sb.append(i + 1).append(". ").append(nullToEmpty(yesterdayItems.get(i))).append('\n');
        }
        return sb.toString();
    }

    private String buildReflectionBlock(RecReflectionVo reflection) {
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

    private String formatDeadline(java.util.Date deadLine) {
        if (deadLine == null) {
            return "无截止日期";
        }
        return DateUtils.parseDateToStr(FormatsType.YYYY_MM_DD, deadLine);
    }

    private String statusLabel(String status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case "completed" -> "已完成";
            case "cancel" -> "已取消";
            case "paused" -> "暂停中";
            case "in_progress", "in-progress" -> "进行中";
            case "pending" -> "待处理";
            default -> status;
        };
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
