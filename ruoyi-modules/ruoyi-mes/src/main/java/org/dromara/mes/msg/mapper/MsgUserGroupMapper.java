package org.dromara.mes.msg.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.msg.domain.MsgUserGroup;
import org.dromara.mes.msg.domain.bo.MsgUserGroupBo;
import org.dromara.mes.msg.domain.vo.MsgUserGroupVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 用户组Mapper接口
 *
 * @author zhout
 * @date 2025-06-08
 */
@Mapper
public interface MsgUserGroupMapper extends BaseMapperPlus<MsgUserGroup, MsgUserGroupVo> {

    /**
     * 查询用户组列表
     *
     * @param bo 用户组
     * @return 用户组
     */
    Page<MsgUserGroupVo> queryPageList(Page<Object> build, @Param("param")  MsgUserGroupBo bo);
}
