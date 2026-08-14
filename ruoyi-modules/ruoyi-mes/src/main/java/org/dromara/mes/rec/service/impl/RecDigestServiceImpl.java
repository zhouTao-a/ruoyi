package org.dromara.mes.rec.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.dromara.mes.rec.mapper.RecGoalMapper;
import org.dromara.mes.rec.mapper.RecTaskMapper;
import org.dromara.mes.rec.service.IRecDigestService;
import org.dromara.mes.rec.service.IRecReflectionService;
import org.dromara.mes.rec.support.RecDigestMailSender;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 每日任务/目标摘要
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RecDigestServiceImpl implements IRecDigestService {

    private final RecTaskMapper recTaskMapper;
    private final RecGoalMapper recGoalMapper;
    private final IRecReflectionService recReflectionService;
    private final ISysUserService sysUserService;
    private final RecDigestMailSender recDigestMailSender;

    @Value("${msg.notify.reflection-username:allen}")
    private String reflectionUsername;

    @Override
    public String sendDailyDigest() {
        Long userId = resolveAllenUserId();
        Date todayStart = todayStart();
        List<RecTaskVo> tasks = TenantHelper.ignore(() -> recTaskMapper.selectCurrentByUserId(userId, todayStart));
        List<RecGoalVo> goals = TenantHelper.ignore(() -> recGoalMapper.selectCurrentByUserId(userId, todayStart));
        RecReflectionVo reflection = pickReflection(userId);
        return sendOrFail(tasks, goals, reflection);
    }

    @Override
    public String testSendLatest() {
        // 试发与定时任务使用同一套当天查询，便于核对真实邮件内容
        return sendDailyDigest();
    }

    private String sendOrFail(List<RecTaskVo> tasks, List<RecGoalVo> goals, RecReflectionVo reflection) {
        String content = recDigestMailSender.buildContent(tasks, goals, reflection);
        if (!recDigestMailSender.sendDigest(tasks, goals, reflection)) {
            throw new ServiceException("日报邮件发送失败");
        }
        return content;
    }

    private RecReflectionVo pickReflection(Long userId) {
        try {
            return TenantHelper.ignore(() -> recReflectionService.pickRandom(userId));
        } catch (Exception e) {
            log.warn("抽取感想失败", e);
            return null;
        }
    }

    private Long resolveAllenUserId() {
        if (StringUtils.isBlank(reflectionUsername)) {
            throw new ServiceException("未配置 msg.notify.reflection-username");
        }
        SysUserVo user = TenantHelper.ignore(() -> sysUserService.selectUserByUserName(reflectionUsername));
        if (user == null || user.getUserId() == null) {
            throw new ServiceException("未找到用户：" + reflectionUsername);
        }
        return user.getUserId();
    }

    private Date todayStart() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
