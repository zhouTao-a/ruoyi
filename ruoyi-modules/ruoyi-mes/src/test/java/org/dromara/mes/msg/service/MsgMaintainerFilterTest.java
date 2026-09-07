package org.dromara.mes.msg.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.mes.msg.domain.bo.MsgDayMatterUserBo;
import org.dromara.mes.msg.domain.bo.MsgGroupBo;
import org.dromara.mes.msg.domain.bo.MsgMatterGroupBo;
import org.dromara.mes.msg.domain.bo.MsgUserBo;
import org.dromara.mes.msg.domain.bo.MsgUserGroupBo;
import org.dromara.mes.msg.mapper.MsgDayMatterUserMapper;
import org.dromara.mes.msg.mapper.MsgGroupMapper;
import org.dromara.mes.msg.mapper.MsgMatterGroupMapper;
import org.dromara.mes.msg.mapper.MsgUserGroupMapper;
import org.dromara.mes.msg.mapper.MsgUserMapper;
import org.dromara.mes.msg.service.impl.MsgDayMatterUserServiceImpl;
import org.dromara.mes.msg.service.impl.MsgGroupServiceImpl;
import org.dromara.mes.msg.service.impl.MsgMatterGroupServiceImpl;
import org.dromara.mes.msg.service.impl.MsgUserGroupServiceImpl;
import org.dromara.mes.msg.service.impl.MsgUserServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 通知模块查询强制按当前登录人 create_by 过滤。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("通知模块维护人隔离")
class MsgMaintainerFilterTest {

    @Mock
    private MsgUserMapper msgUserMapper;
    @Mock
    private MsgGroupMapper msgGroupMapper;
    @Mock
    private MsgUserGroupMapper msgUserGroupMapper;
    @Mock
    private MsgDayMatterUserMapper msgDayMatterUserMapper;
    @Mock
    private MsgMatterGroupMapper msgMatterGroupMapper;

    @InjectMocks
    private MsgUserServiceImpl msgUserService;
    @InjectMocks
    private MsgGroupServiceImpl msgGroupService;
    @InjectMocks
    private MsgUserGroupServiceImpl msgUserGroupService;
    @InjectMocks
    private MsgDayMatterUserServiceImpl msgDayMatterUserService;
    @InjectMocks
    private MsgMatterGroupServiceImpl msgMatterGroupService;

    private MockedStatic<LoginHelper> loginHelperMock;

    @BeforeEach
    void setUp() {
        loginHelperMock = mockStatic(LoginHelper.class);
        loginHelperMock.when(LoginHelper::getUserId).thenReturn(1L);
    }

    @AfterEach
    void tearDown() {
        if (loginHelperMock != null) {
            loginHelperMock.close();
        }
    }

    @Test
    @Tag("dev")
    @DisplayName("用户列表强制当前登录人")
    void userList_overridesCreateBy() {
        when(msgUserMapper.queryPageList(any(), any())).thenReturn(new Page<>());
        MsgUserBo bo = new MsgUserBo();
        bo.setCreateBy(999L);

        msgUserService.queryPageList(bo, new PageQuery());

        ArgumentCaptor<MsgUserBo> captor = ArgumentCaptor.forClass(MsgUserBo.class);
        verify(msgUserMapper).queryPageList(any(), captor.capture());
        assertEquals(1L, captor.getValue().getCreateBy());
    }

    @Test
    @Tag("dev")
    @DisplayName("用户下拉强制当前登录人")
    void userCodeList_passesCreateBy() {
        when(msgUserMapper.queryUserCodePageList(any(), any(), any(), any())).thenReturn(new Page<>());

        msgUserService.queryUserCodePageList("张", null, new PageQuery());

        verify(msgUserMapper).queryUserCodePageList(any(), eq(null), eq("张"), eq(1L));
    }

    @Test
    @Tag("dev")
    @DisplayName("分组列表强制当前登录人")
    void groupList_overridesCreateBy() {
        when(msgGroupMapper.queryPageList(any(), any())).thenReturn(new Page<>());
        MsgGroupBo bo = new MsgGroupBo();
        bo.setCreateBy(999L);

        msgGroupService.queryPageList(bo, new PageQuery());

        ArgumentCaptor<MsgGroupBo> captor = ArgumentCaptor.forClass(MsgGroupBo.class);
        verify(msgGroupMapper).queryPageList(any(), captor.capture());
        assertEquals(1L, captor.getValue().getCreateBy());
    }

    @Test
    @Tag("dev")
    @DisplayName("分组下拉强制当前登录人")
    void groupCodeList_passesCreateBy() {
        msgGroupService.queryGroupCodePageList("家", null, new PageQuery());

        verify(msgGroupMapper).queryGroupCodePageList(any(), eq("家"), eq(null), eq(1L));
    }

    @Test
    @Tag("dev")
    @DisplayName("用户组列表强制当前登录人")
    void userGroupList_overridesCreateBy() {
        when(msgUserGroupMapper.queryPageList(any(), any())).thenReturn(new Page<>());
        MsgUserGroupBo bo = new MsgUserGroupBo();
        bo.setCreateBy(999L);

        msgUserGroupService.queryPageList(bo, new PageQuery());

        ArgumentCaptor<MsgUserGroupBo> captor = ArgumentCaptor.forClass(MsgUserGroupBo.class);
        verify(msgUserGroupMapper).queryPageList(any(), captor.capture());
        assertEquals(1L, captor.getValue().getCreateBy());
    }

    @Test
    @Tag("dev")
    @DisplayName("用户事件列表强制当前登录人")
    void dayMatterUserList_overridesCreateBy() {
        when(msgDayMatterUserMapper.queryPageList(any(), any())).thenReturn(new Page<>());
        MsgDayMatterUserBo bo = new MsgDayMatterUserBo();
        bo.setCreateBy(999L);

        msgDayMatterUserService.queryPageList(bo, new PageQuery());

        ArgumentCaptor<MsgDayMatterUserBo> captor = ArgumentCaptor.forClass(MsgDayMatterUserBo.class);
        verify(msgDayMatterUserMapper).queryPageList(any(), captor.capture());
        assertEquals(1L, captor.getValue().getCreateBy());
    }

    @Test
    @Tag("dev")
    @DisplayName("事件组列表强制当前登录人")
    void matterGroupList_overridesCreateBy() {
        when(msgMatterGroupMapper.queryPageList(any(), any())).thenReturn(new Page<>());
        MsgMatterGroupBo bo = new MsgMatterGroupBo();
        bo.setCreateBy(999L);

        msgMatterGroupService.queryPageList(bo, new PageQuery());

        ArgumentCaptor<MsgMatterGroupBo> captor = ArgumentCaptor.forClass(MsgMatterGroupBo.class);
        verify(msgMatterGroupMapper).queryPageList(any(), captor.capture());
        assertEquals(1L, captor.getValue().getCreateBy());
    }
}
