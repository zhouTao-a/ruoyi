package org.dromara.mes.mock.mockito;

import org.dromara.mes.msg.domain.MsgUser;
import org.dromara.mes.msg.mapper.MsgUserMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

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
}
