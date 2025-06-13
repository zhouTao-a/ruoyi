package org.dromara.mes.msg.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.msg.domain.MsgUser;
import org.dromara.mes.msg.domain.bo.MsgUserBo;
import org.dromara.mes.msg.domain.vo.MsgUserCodeVo;
import org.dromara.mes.msg.domain.vo.MsgUserVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 用户Mapper接口
 *
 * @author zhout
 * @date 2025-06-08
 */
public interface MsgUserMapper extends BaseMapperPlus<MsgUser, MsgUserVo> {

    /**
     * 查询用户列表
     *
     * @param bo 用户
     * @return 用户
     */
    Page<MsgUserCodeVo> queryUserCodePageList(Page<Object> build,
                                              @Param("param") MsgUserBo bo);
}
