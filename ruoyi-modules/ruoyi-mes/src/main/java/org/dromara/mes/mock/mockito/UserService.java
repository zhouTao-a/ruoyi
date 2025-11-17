package org.dromara.mes.mock.mockito;

import org.dromara.mes.msg.domain.MsgUser;
import org.dromara.mes.msg.mapper.MsgUserMapper;

import java.util.List;

public class UserService {

    private final MsgUserMapper mapper;

    public UserService(MsgUserMapper mapper) {
        this.mapper = mapper;
    }

    public String getUserName(Long id) {
        return mapper.selectById(id).getUserName();
    }

    public List<MsgUser> getUserList(){
        List<MsgUser> msgUsers = mapper.selectList();
        msgUsers.forEach(msgUser -> msgUser.setUserName(msgUser.getUserName() + "单元测试"));
        msgUsers.remove(1);
        msgUsers.remove(1);
        return msgUsers;
    }
}
