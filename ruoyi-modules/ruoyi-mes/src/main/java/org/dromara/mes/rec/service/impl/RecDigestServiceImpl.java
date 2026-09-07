package org.dromara.mes.rec.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.domain.vo.RecIntrospectDigestVo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.dromara.mes.rec.mapper.RecGoalMapper;
import org.dromara.mes.rec.mapper.RecTaskMapper;
import org.dromara.mes.rec.service.IRecDigestService;
import org.dromara.mes.rec.service.IRecIntrospectService;
import org.dromara.mes.rec.service.IRecReflectionService;
import org.dromara.mes.rec.support.RecDigestMailSender;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 每日任务/目标/自省摘要
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RecDigestServiceImpl implements IRecDigestService {

    private final RecTaskMapper recTaskMapper;
    private final RecGoalMapper recGoalMapper;
    private final IRecIntrospectService recIntrospectService;
    private final IRecReflectionService recReflectionService;
    private final ISysUserService sysUserService;
    private final RecDigestMailSender recDigestMailSender;

    @Value("${msg.notify.reflection-username:allen}")
    private String reflectionUsername;

    @Override
    public String sendDailyDigest() {
        return sendDailyDigest(null);
    }

    @Override
    public String sendDailyDigest(String jobParamsJson) {
        List<String> userNames = parseUserNames(jobParamsJson);
        if (userNames.isEmpty() && StringUtils.isNotBlank(reflectionUsername)) {
            userNames = List.of(reflectionUsername.trim());
        }
        if (userNames.isEmpty()) {
            throw new ServiceException("未指定日报用户");
        }
        List<String> contents = new ArrayList<>();
        for (String userName : userNames) {
            String content = sendForUser(userName);
            if (StringUtils.isNotBlank(content)) {
                contents.add(content);
            }
        }
        if (contents.isEmpty()) {
            throw new ServiceException("没有可发送的日报（用户不存在或未维护邮箱）");
        }
        return String.join("\n----\n", contents);
    }

    @Override
    public String testSendLatest() {
        // 试发与定时任务使用同一套当天查询，便于核对真实邮件内容
        return sendDailyDigest();
    }

    /**
     * 给指定系统用户发送其自己的日报；找不到人或没有邮箱则跳过。
     */
    private String sendForUser(String userName) {
        SysUserVo user = TenantHelper.ignore(() -> sysUserService.selectUserByUserName(userName));
        if (user == null || user.getUserId() == null) {
            log.warn("日报跳过：未找到用户 username={}", userName);
            return null;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            log.warn("日报跳过：用户未维护邮箱 username={}", userName);
            return null;
        }
        Long userId = user.getUserId();
        Date todayStart = todayStart();
        List<RecTaskVo> tasks = TenantHelper.ignore(() -> recTaskMapper.selectCurrentByUserId(userId, todayStart));
        List<RecGoalVo> goals = TenantHelper.ignore(() -> recGoalMapper.selectCurrentByUserId(userId, todayStart));
        RecIntrospectDigestVo introspect = recIntrospectService.queryDigestByUserId(userId);
        RecReflectionVo reflection = pickReflection(userId);
        String content = recDigestMailSender.buildContent(tasks, goals, introspect, reflection);
        if (!recDigestMailSender.sendDigest(tasks, goals, introspect, reflection, user.getEmail())) {
            log.warn("日报发送失败 username={} email={}", userName, user.getEmail());
            return null;
        }
        return content;
    }

    /**
     * 解析 {"userName":"allen,hanhan"}，按英文逗号拆分。
     */
    private List<String> parseUserNames(String jobParamsJson) {
        if (StringUtils.isBlank(jobParamsJson)) {
            return List.of();
        }
        try {
            JSONObject obj = JSONUtil.parseObj(jobParamsJson);
            String userName = obj.getStr("userName");
            if (StringUtils.isBlank(userName)) {
                return List.of();
            }
            Set<String> names = new LinkedHashSet<>();
            for (String item : StringUtils.splitList(userName, ",")) {
                if (StringUtils.isNotBlank(item)) {
                    names.add(item.trim());
                }
            }
            return List.copyOf(names);
        } catch (Exception e) {
            log.warn("日报任务参数无法解析: {}", jobParamsJson, e);
            return List.of();
        }
    }

    private RecReflectionVo pickReflection(Long userId) {
        try {
            return TenantHelper.ignore(() -> recReflectionService.pickRandom(userId));
        } catch (Exception e) {
            log.warn("抽取感想失败", e);
            return null;
        }
    }

    private Date todayStart() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
