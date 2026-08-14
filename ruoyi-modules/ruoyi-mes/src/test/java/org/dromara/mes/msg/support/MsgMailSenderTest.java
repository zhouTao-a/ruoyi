package org.dromara.mes.msg.support;

import org.dromara.common.core.enums.FormatsType;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.service.IRecReflectionService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("dev")
@ExtendWith(MockitoExtension.class)
@DisplayName("事件邮件正文")
class MsgMailSenderTest {

    private static final Long ALLEN_USER_ID = 100L;

    @Mock
    private IRecReflectionService recReflectionService;

    @Mock
    private ISysUserService sysUserService;

    @InjectMocks
    private MsgMailSender msgMailSender;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(msgMailSender, "recUrl", "http://www.allen-z.cn/rec-total");
        ReflectionTestUtils.setField(msgMailSender, "reflectionUsername", "allen");
    }

    @Test
    @DisplayName("正文顺序为事件、感想、访问地址，感想固定取 allen")
    void buildContent_orderIsEventThenReflectionThenUrl() {
        stubAllen();
        RecReflectionVo reflection = new RecReflectionVo();
        reflection.setTitle("读完一本书");
        reflection.setSynopsis("概要内容");
        reflection.setContent("感想正文");
        reflection.setSourceName("某书");
        when(recReflectionService.pickRandom(eq(ALLEN_USER_ID))).thenReturn(reflection);

        String body = msgMailSender.buildContent(sampleEvent());

        int eventPos = body.indexOf("【事件】");
        int reflectionPos = body.indexOf("【感想】");
        int urlPos = body.indexOf("访问地址：http://www.allen-z.cn/rec-total");
        assertTrue(eventPos >= 0 && eventPos < reflectionPos);
        assertTrue(reflectionPos < urlPos);
        assertTrue(body.contains("事件名称：小周生日"));
        assertTrue(body.contains("标题：读完一本书"));
        assertTrue(body.contains("感想：感想正文"));
        verify(recReflectionService).pickRandom(ALLEN_USER_ID);
        verify(recReflectionService, never()).pickRandom(eq(9L));
    }

    @Test
    @DisplayName("没有感想时仍带事件和访问地址")
    void buildContent_noReflection_keepsEventAndUrl() {
        stubAllen();
        when(recReflectionService.pickRandom(eq(ALLEN_USER_ID))).thenReturn(null);

        String body = msgMailSender.buildContent(sampleEvent());

        assertTrue(body.contains("【事件】"));
        assertTrue(body.contains("暂无感想"));
        assertTrue(body.contains("访问地址：http://www.allen-z.cn/rec-total"));
    }

    private void stubAllen() {
        SysUserVo allen = new SysUserVo();
        allen.setUserId(ALLEN_USER_ID);
        allen.setUserName("allen");
        when(sysUserService.selectUserByUserName(eq("allen"))).thenReturn(allen);
    }

    private MsgDayMatterVo sampleEvent() {
        MsgDayMatterVo vo = new MsgDayMatterVo();
        vo.setDayName("小周生日");
        vo.setDayType("birthday");
        vo.setUserId(9L);
        vo.setNextNotifyTime(DateUtils.parseDateTime(FormatsType.YYYY_MM_DD_HH_MM_SS, "2026-08-15 06:00:00"));
        return vo;
    }
}
