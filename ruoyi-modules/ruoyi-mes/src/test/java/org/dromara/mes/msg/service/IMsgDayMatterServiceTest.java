package org.dromara.mes.msg.service;


import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.dromara.common.core.enums.FormatsType;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.mes.msg.enums.WhetherFlag;
import org.dromara.mes.msg.mapper.MsgDayMatterMapper;
import org.dromara.mes.msg.service.impl.MsgDayMatterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.apache.ibatis.session.Configuration;

@ExtendWith(MockitoExtension.class)
@DisplayName("事件服务单元测试")
public class IMsgDayMatterServiceTest {

    @Mock
    private MsgDayMatterMapper msgDayMatterMapper;

    // 使用具体的实现类
    @InjectMocks
    private MsgDayMatterServiceImpl msgDayMatterService;

    private final Page<MsgDayMatterVo> page = new Page<>();
    private final List<MsgDayMatterVo> msgDayMatterList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
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

    }

    @Test
    @DisplayName("定时更新下次提醒时间 - 测试")
    public void updateNextNotifyTime() {

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
        verify(msgDayMatterMapper, times(1)).update(any());
    }


}
