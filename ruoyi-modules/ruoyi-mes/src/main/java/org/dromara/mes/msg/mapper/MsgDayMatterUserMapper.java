package org.dromara.mes.msg.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.msg.domain.MsgDayMatterUser;
import org.dromara.mes.msg.domain.bo.MsgDayMatterUserBo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterUserVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 事件与用户关联Mapper接口
 *
 * @author zhout
 * @date 2025-06-08
 */
@Mapper
public interface MsgDayMatterUserMapper extends BaseMapperPlus<MsgDayMatterUser, MsgDayMatterUserVo> {

    /**
     * 查询事件与用户关联列表
     *
     * @param bo 事件与用户关联
     * @return 事件与用户关联
     */
    Page<MsgDayMatterUserVo> queryPageList(Page<Object> build,
                                           @Param("param") MsgDayMatterUserBo bo);
}
