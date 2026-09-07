package org.dromara.mes.rec.service;

import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.domain.vo.RecIntrospectDigestVo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.dromara.mes.rec.mapper.RecGoalMapper;
import org.dromara.mes.rec.mapper.RecTaskMapper;
import org.dromara.mes.rec.service.impl.RecDigestServiceImpl;
import org.dromara.mes.rec.support.RecDigestMailSender;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("dev")
@ExtendWith(MockitoExtension.class)
@DisplayName("日报摘要服务")
class RecDigestServiceTest {

    private static final Long ALLEN_USER_ID = 100L;

    @Mock
    private RecTaskMapper recTaskMapper;

    @Mock
    private RecGoalMapper recGoalMapper;

    @Mock
    private IRecIntrospectService recIntrospectService;

    @Mock
    private IRecReflectionService recReflectionService;

    @Mock
    private ISysUserService sysUserService;

    @Mock
    private RecDigestMailSender recDigestMailSender;

    @InjectMocks
    private RecDigestServiceImpl recDigestService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(recDigestService, "reflectionUsername", "allen");
        SysUserVo allen = new SysUserVo();
        allen.setUserId(ALLEN_USER_ID);
        allen.setUserName("allen");
        allen.setEmail("allen@test.com");
        when(sysUserService.selectUserByUserName(eq("allen"))).thenReturn(allen);
    }

    @Test
    @DisplayName("试发按当天实际条件查询任务和目标")
    void testSendLatest_usesCurrentQuery() {
        RecTaskVo task = new RecTaskVo();
        task.setTitle("逾期任务");
        RecGoalVo goal = new RecGoalVo();
        goal.setTitle("进行中目标");
        RecIntrospectDigestVo introspect = new RecIntrospectDigestVo();
        introspect.setTodayTitle("控制情绪");
        RecReflectionVo reflection = new RecReflectionVo();
        reflection.setTitle("随机感想");
        when(recTaskMapper.selectCurrentByUserId(eq(ALLEN_USER_ID), any())).thenReturn(List.of(task));
        when(recGoalMapper.selectCurrentByUserId(eq(ALLEN_USER_ID), any())).thenReturn(List.of(goal));
        when(recIntrospectService.queryDigestByUserId(eq(ALLEN_USER_ID))).thenReturn(introspect);
        when(recReflectionService.pickRandom(eq(ALLEN_USER_ID))).thenReturn(reflection);
        when(recDigestMailSender.buildContent(any(), any(), any(), any())).thenReturn("正文");
        when(recDigestMailSender.sendDigest(any(), any(), any(), any(), eq("allen@test.com"))).thenReturn(true);

        String result = recDigestService.testSendLatest();

        assertEquals("正文", result);
        verify(recTaskMapper).selectCurrentByUserId(eq(ALLEN_USER_ID), any());
        verify(recGoalMapper).selectCurrentByUserId(eq(ALLEN_USER_ID), any());
        verify(recIntrospectService).queryDigestByUserId(ALLEN_USER_ID);
        verify(recReflectionService).pickRandom(ALLEN_USER_ID);
        verify(recDigestMailSender).sendDigest(any(), any(), any(), any(), eq("allen@test.com"));
    }

    @Test
    @DisplayName("按任务参数用户名发信，无邮箱的跳过")
    void sendDailyDigest_sendsOnlyUsersWithEmail() {
        SysUserVo hanhan = new SysUserVo();
        hanhan.setUserId(200L);
        hanhan.setUserName("hanhan");
        hanhan.setEmail("hanhan@test.com");
        when(sysUserService.selectUserByUserName(eq("hanhan"))).thenReturn(hanhan);
        when(recTaskMapper.selectCurrentByUserId(any(), any())).thenReturn(List.of());
        when(recGoalMapper.selectCurrentByUserId(any(), any())).thenReturn(List.of());
        when(recIntrospectService.queryDigestByUserId(any())).thenReturn(null);
        when(recReflectionService.pickRandom(any())).thenReturn(null);
        when(recDigestMailSender.buildContent(any(), any(), any(), any())).thenReturn("正文");
        when(recDigestMailSender.sendDigest(any(), any(), any(), any(), any())).thenReturn(true);

        recDigestService.sendDailyDigest("{\"userName\":\"allen,hanhan\"}");

        verify(recDigestMailSender).sendDigest(any(), any(), any(), any(), eq("allen@test.com"));
        verify(recDigestMailSender).sendDigest(any(), any(), any(), any(), eq("hanhan@test.com"));
    }

    @Test
    @DisplayName("用户不存在或未维护邮箱则跳过")
    void sendDailyDigest_skipsMissingUserAndBlankEmail() {
        SysUserVo noMail = new SysUserVo();
        noMail.setUserId(200L);
        noMail.setUserName("hanhan");
        when(sysUserService.selectUserByUserName(eq("hanhan"))).thenReturn(noMail);
        when(sysUserService.selectUserByUserName(eq("ghost"))).thenReturn(null);
        when(recTaskMapper.selectCurrentByUserId(eq(ALLEN_USER_ID), any())).thenReturn(List.of());
        when(recGoalMapper.selectCurrentByUserId(eq(ALLEN_USER_ID), any())).thenReturn(List.of());
        when(recIntrospectService.queryDigestByUserId(eq(ALLEN_USER_ID))).thenReturn(null);
        when(recReflectionService.pickRandom(eq(ALLEN_USER_ID))).thenReturn(null);
        when(recDigestMailSender.buildContent(any(), any(), any(), any())).thenReturn("正文");
        when(recDigestMailSender.sendDigest(any(), any(), any(), any(), eq("allen@test.com"))).thenReturn(true);

        recDigestService.sendDailyDigest("{\"userName\":\"ghost,hanhan,allen\"}");

        verify(recDigestMailSender).sendDigest(any(), any(), any(), any(), eq("allen@test.com"));
        verify(recDigestMailSender, never()).sendDigest(any(), any(), any(), any(), eq("hanhan@test.com"));
    }
}
