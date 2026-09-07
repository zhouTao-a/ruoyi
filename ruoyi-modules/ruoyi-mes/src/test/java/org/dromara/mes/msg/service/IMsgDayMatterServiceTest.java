package org.dromara.mes.msg.service;


import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.dromara.common.core.enums.FormatsType;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.mes.msg.domain.bo.MsgDayMatterBo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.mes.msg.domain.vo.ReminderVo;
import org.dromara.mes.msg.enums.WhetherFlag;
import org.dromara.mes.msg.mapper.MsgDayMatterMapper;
import org.dromara.mes.msg.mapper.MsgMatterGroupMapper;
import org.dromara.mes.msg.service.impl.MsgDayMatterServiceImpl;
import org.dromara.mes.msg.support.MsgMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.apache.ibatis.session.Configuration;

@ExtendWith(MockitoExtension.class)
@DisplayName("事件服务单元测试")
public class IMsgDayMatterServiceTest {

    @Mock
    private MsgDayMatterMapper msgDayMatterMapper;

    @Mock
    private MsgMatterGroupMapper msgMatterGroupMapper;

    @Mock
    private MsgMailSender msgMailSender;

    // 使用具体的实现类
    @InjectMocks
    private MsgDayMatterServiceImpl msgDayMatterService;

    private final Page<MsgDayMatterVo> page = new Page<>();
    private final List<MsgDayMatterVo> msgDayMatterList = new ArrayList<>();
    private MockedStatic<LoginHelper> loginHelperMock;

    @BeforeEach
    public void setUp() {
        loginHelperMock = mockStatic(LoginHelper.class);
        loginHelperMock.when(LoginHelper::getUserId).thenReturn(1L);

        // 初始化 MyBatis-Plus 的 TableInfo
        Configuration configuration = new Configuration();
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(configuration, "");
        TableInfoHelper.initTableInfo(assistant, MsgDayMatter.class);

        Date date = DateUtils.parseDateTime(FormatsType.YYYY_MM_DD_HH_MM_SS, "2025-05-20 00:13:14");
        MsgDayMatterVo vo = new MsgDayMatterVo();
        vo.setId(1L);
        vo.setDayName("公历分钟测试");
        vo.setDayTarget(date);
        vo.setDayLunar("solar");
        vo.setDayType("life");
        vo.setRemindType("minutely");
        vo.setRepeatFlag(WhetherFlag.YES.getCode());
        msgDayMatterList.add(vo);

        page.setRecords(msgDayMatterList);
        page.setTotal(msgDayMatterList.size());
        ReflectionTestUtils.invokeMethod(msgDayMatterService, "invalidateCalendarEventCache");
    }

    @AfterEach
    public void tearDown() {
        if (loginHelperMock != null) {
            loginHelperMock.close();
        }
    }

    @Test
    @Tag("dev")
    @DisplayName("定时更新下次提醒时间 - 测试")
    public void updateNextNotifyTime() {
        when(msgMailSender.sendDayMatter(any())).thenReturn(true);
        when(msgDayMatterMapper.queryPageList(any(), any())).thenReturn(page);
        when(msgDayMatterMapper.update(any())).thenReturn(1);

        msgDayMatterService.updateNextNotifyTime();

        assert page.getTotal() == 1;
        String nextNotifyTime = DateUtils.parseDateToStr(FormatsType.YYYY_MM_DD_HH_MM,
            DateUtils.addMinutes(DateUtils.getNowDate(), 1));
        System.out.println("下次执行时间：" + nextNotifyTime);
        assertEquals(DateUtils.parseDateToStr(FormatsType.YYYY_MM_DD_HH_MM,
            msgDayMatterList.get(0).getNextNotifyTime()), nextNotifyTime);

        verify(msgDayMatterMapper, times(1)).queryPageList(any(), any());
        verify(msgMailSender, times(1)).sendDayMatter(any());
        verify(msgDayMatterMapper, times(1)).update(any());
    }

    @Test
    @Tag("dev")
    @DisplayName("邮件发送失败时不滚动下次时间")
    public void updateNextNotifyTime_sendFail_keepsDueTime() {
        when(msgMailSender.sendDayMatter(any())).thenReturn(false);
        when(msgDayMatterMapper.queryPageList(any(), any())).thenReturn(page);

        msgDayMatterService.updateNextNotifyTime();

        verify(msgMailSender, times(1)).sendDayMatter(any());
        verify(msgDayMatterMapper, never()).update(any());
    }

    @Test
    @Tag("dev")
    @DisplayName("已停用事件不发邮件")
    public void updateNextNotifyTime_disabled_skipsSend() {
        msgDayMatterList.get(0).setNotifyStatus("disabled");
        when(msgDayMatterMapper.queryPageList(any(), any())).thenReturn(page);

        msgDayMatterService.updateNextNotifyTime();

        verify(msgMailSender, never()).sendDayMatter(any());
        verify(msgDayMatterMapper, never()).update(any());
    }

    @Test
    @Tag("dev")
    @DisplayName("试发最近一条事件邮件，不滚动下次时间")
    public void testSendLatestMail_sendsWithoutUpdatingNextTime() {
        when(msgDayMatterMapper.selectLatestOne()).thenReturn(msgDayMatterList.get(0));
        when(msgMailSender.sendDayMatter(any())).thenReturn(true);

        String result = msgDayMatterService.testSendLatestMail();

        assertEquals("已发送：公历分钟测试", result);
        verify(msgMailSender, times(1)).sendDayMatter(any());
        verify(msgDayMatterMapper, never()).update(any());
    }

    @Test
    @Tag("dev")
    @DisplayName("日历列表 - 每年循环事件应展示查询年当月，而不是下次通知年")
    public void dayMatterList_yearlyRepeat_projectsToQueryYear() {
        MsgDayMatterVo vo = yearlySolar("生日", "2025-03-15 00:00:00", "2027-03-15 00:00:00");
        stubMatterList(List.of(vo));

        List<ReminderVo> result = msgDayMatterService.dayMatterList(2026, 3, null);

        assertEquals(1, result.size());
        assertEquals("2026-03-15", result.get(0).getDate());
        assertEquals("生日", result.get(0).getContent());
        assertEquals("birthday", result.get(0).getType());
    }

    @Test
    @Tag("dev")
    @DisplayName("日历列表 - 每年循环事件在非目标月不展示")
    public void dayMatterList_yearlyRepeat_otherMonthEmpty() {
        MsgDayMatterVo vo = yearlySolar("生日", "2025-03-15 00:00:00", "2027-03-15 00:00:00");
        stubMatterList(List.of(vo));

        List<ReminderVo> result = msgDayMatterService.dayMatterList(2026, 4, null);

        assertTrue(result.isEmpty());
    }

    @Test
    @Tag("dev")
    @DisplayName("日历列表 - 不循环事件只展示原始日期所在年")
    public void dayMatterList_nonRepeat_onlyOriginalYear() {
        MsgDayMatterVo vo = yearlySolar("一次性", "2025-03-15 00:00:00", "2027-03-15 00:00:00");
        vo.setRepeatFlag(WhetherFlag.NO.getCode());
        stubMatterList(List.of(vo));

        assertTrue(msgDayMatterService.dayMatterList(2026, 3, null).isEmpty());
        List<ReminderVo> originalYear = msgDayMatterService.dayMatterList(2025, 3, null);
        assertEquals(1, originalYear.size());
        assertEquals("2025-03-15", originalYear.get(0).getDate());
    }

    @Test
    @Tag("dev")
    @DisplayName("日历列表 - 每月循环事件投影到查询月")
    public void dayMatterList_monthlyRepeat_projectsToQueryMonth() {
        MsgDayMatterVo vo = yearlySolar("还款日", "2025-01-20 00:00:00", "2027-01-20 00:00:00");
        vo.setRemindType("monthly");
        vo.setDayType("work");
        stubMatterList(List.of(vo));

        List<ReminderVo> result = msgDayMatterService.dayMatterList(2026, 8, null);

        assertEquals(1, result.size());
        assertEquals("2026-08-20", result.get(0).getDate());
    }

    @Test
    @Tag("dev")
    @DisplayName("日历列表 - 只查询当前登录人维护的事件")
    public void dayMatterList_filtersByLoginUserCreateBy() {
        stubMatterList(List.of(yearlySolar("生日", "2025-03-15 00:00:00", "2027-03-15 00:00:00")));

        msgDayMatterService.dayMatterList(2026, 3, null);

        ArgumentCaptor<MsgDayMatterBo> captor = ArgumentCaptor.forClass(MsgDayMatterBo.class);
        verify(msgDayMatterMapper).queryPageList(any(), captor.capture());
        assertEquals(1L, captor.getValue().getCreateBy());
    }

    @Test
    @Tag("dev")
    @DisplayName("管理列表 - 忽略入参 createBy，强制当前登录人")
    public void queryPageList_overridesCreateByToLoginUser() {
        when(msgDayMatterMapper.queryPageList(any(), any())).thenReturn(page);
        MsgDayMatterBo bo = new MsgDayMatterBo();
        bo.setCreateBy(999L);

        msgDayMatterService.queryPageList(bo, new PageQuery());

        ArgumentCaptor<MsgDayMatterBo> captor = ArgumentCaptor.forClass(MsgDayMatterBo.class);
        verify(msgDayMatterMapper).queryPageList(any(), captor.capture());
        assertEquals(1L, captor.getValue().getCreateBy());
    }

    @Test
    @Tag("dev")
    @DisplayName("日历列表 - 缓存按登录人隔离")
    public void dayMatterList_cacheIsolatedByUser() {
        MsgDayMatterVo user1Event = yearlySolar("用户1生日", "2025-03-15 00:00:00", "2027-03-15 00:00:00");
        MsgDayMatterVo user2Event = yearlySolar("用户2生日", "2025-03-16 00:00:00", "2027-03-16 00:00:00");
        user2Event.setId(3L);
        Page<MsgDayMatterVo> page1 = new Page<>();
        page1.setRecords(List.of(user1Event));
        Page<MsgDayMatterVo> page2 = new Page<>();
        page2.setRecords(List.of(user2Event));
        when(msgDayMatterMapper.queryPageList(any(), any())).thenReturn(page1, page2);

        loginHelperMock.when(LoginHelper::getUserId).thenReturn(1L);
        List<ReminderVo> first = msgDayMatterService.dayMatterList(2026, 3, null);
        loginHelperMock.when(LoginHelper::getUserId).thenReturn(2L);
        List<ReminderVo> second = msgDayMatterService.dayMatterList(2026, 3, null);

        assertEquals("用户1生日", first.get(0).getContent());
        assertEquals("用户2生日", second.get(0).getContent());
        verify(msgDayMatterMapper, times(2)).queryPageList(any(), any());
    }

    private MsgDayMatterVo yearlySolar(String name, String target, String nextNotify) {
        MsgDayMatterVo vo = new MsgDayMatterVo();
        vo.setId(2L);
        vo.setDayName(name);
        vo.setDayTarget(DateUtils.parseDateTime(FormatsType.YYYY_MM_DD_HH_MM_SS, target));
        vo.setDayLunar("solar");
        vo.setDayType("birthday");
        vo.setRemindType("yearly");
        vo.setRepeatFlag(WhetherFlag.YES.getCode());
        vo.setNextNotifyTime(DateUtils.parseDateTime(FormatsType.YYYY_MM_DD_HH_MM_SS, nextNotify));
        return vo;
    }

    private void stubMatterList(List<MsgDayMatterVo> records) {
        Page<MsgDayMatterVo> matterPage = new Page<>();
        matterPage.setRecords(records);
        matterPage.setTotal(records.size());
        when(msgDayMatterMapper.queryPageList(any(), any())).thenReturn(matterPage);
    }

}
