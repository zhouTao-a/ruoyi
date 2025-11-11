package org.dromara.mes.mock.mockito;

import org.dromara.mes.msg.mapper.MsgUserMapper;

public class UserService {

    private final MsgUserMapper mapper;

    public UserService(MsgUserMapper mapper) {
        this.mapper = mapper;
    }

    public String getUserName(Long id) {
        return mapper.selectById(id).getUserName();
    }
}
