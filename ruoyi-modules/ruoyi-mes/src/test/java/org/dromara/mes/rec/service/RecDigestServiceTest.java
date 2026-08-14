package org.dromara.mes.rec.service;

import org.dromara.mes.rec.domain.vo.RecGoalVo;
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
        when(sysUserService.selectUserByUserName(eq("allen"))).thenReturn(allen);
    }

    @Test
    @DisplayName("试发按当天实际条件查询任务和目标")
    void testSendLatest_usesCurrentQuery() {
        RecTaskVo task = new RecTaskVo();
        task.setTitle("逾期任务");
        RecGoalVo goal = new RecGoalVo();
        goal.setTitle("进行中目标");
        RecReflectionVo reflection = new RecReflectionVo();
        reflection.setTitle("随机感想");
        when(recTaskMapper.selectCurrentByUserId(eq(ALLEN_USER_ID), any())).thenReturn(List.of(task));
        when(recGoalMapper.selectCurrentByUserId(eq(ALLEN_USER_ID), any())).thenReturn(List.of(goal));
        when(recReflectionService.pickRandom(eq(ALLEN_USER_ID))).thenReturn(reflection);
        when(recDigestMailSender.buildContent(any(), any(), any())).thenReturn("正文");
        when(recDigestMailSender.sendDigest(any(), any(), any())).thenReturn(true);

        String result = recDigestService.testSendLatest();

        assertEquals("正文", result);
        verify(recTaskMapper).selectCurrentByUserId(eq(ALLEN_USER_ID), any());
        verify(recGoalMapper).selectCurrentByUserId(eq(ALLEN_USER_ID), any());
        verify(recReflectionService).pickRandom(ALLEN_USER_ID);
        verify(recDigestMailSender).sendDigest(any(), any(), any());
    }
}
