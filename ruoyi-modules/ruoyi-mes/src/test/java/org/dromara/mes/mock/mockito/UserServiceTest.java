package org.dromara.mes.mock.mockito;

import org.dromara.mes.msg.domain.MsgUser;
import org.dromara.mes.msg.mapper.MsgUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final List<MsgUser> msgUserList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MsgUser msgUser = new MsgUser();
        msgUser.setId(1L);
        msgUser.setUserName("Tony");
        msgUser.setUserCode("Tony");
        msgUserList.add(msgUser);

        msgUser = new MsgUser();
        msgUser.setId(2L);
        msgUser.setUserName("allen");
        msgUser.setUserCode("allen");
        msgUserList.add(msgUser);

        msgUser = new MsgUser();
        msgUser.setId(3L);
        msgUser.setUserName("Tom");
        msgUser.setUserCode("Tom");
        msgUserList.add(msgUser);
    }

    @Test
    void testGetUserName() {
        // 1. mock mapper
        MsgUserMapper mapper = mock(MsgUserMapper.class);

        // 2. 准备返回的假数据
        MsgUser mockUser = new MsgUser();
        mockUser.setUserName("Tony");

        // 3. stub 行为
        when(mapper.selectById(1L)).thenReturn(mockUser);

        // 4. 创建 service 并调用
        UserService service = new UserService(mapper);
        String name = service.getUserName(1L);

        // 5. 断言 & 校验调用
        assertEquals("Tony", name);
        verify(mapper, times(1)).selectById(1L);
    }


    @Test
    void testGetUserList() {
        // 1. mock mapper
        MsgUserMapper mapper = mock(MsgUserMapper.class);

        // 2. stub 行为
        when(mapper.selectList()).thenReturn(msgUserList);

        // 3. 创建 service 并调用
        UserService service = new UserService(mapper);
        List<MsgUser> userList = service.getUserList();

        // 5. 断言 & 校验调用
        assertEquals(1, userList.size());
        assertEquals("Tony单元测试", userList.get(0).getUserName());
        verify(mapper, times(1)).selectList();
    }
}
