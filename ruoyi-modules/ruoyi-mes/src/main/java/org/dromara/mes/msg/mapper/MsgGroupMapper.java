package org.dromara.mes.msg.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.msg.domain.MsgGroup;
import org.dromara.mes.msg.domain.bo.MsgGroupBo;
import org.dromara.mes.msg.domain.vo.MsgGroupVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 分组信息Mapper接口
 *
 * @author zhout
 * @date 2025-06-08
 */
public interface MsgGroupMapper extends BaseMapperPlus<MsgGroup, MsgGroupVo> {

    /**
     * 查询分组信息列表
     *
     * @param bo 分组信息
     * @return 分组信息
     */
    Page<MsgGroupVo> queryPageList(Page<Object> page, @Param("param") MsgGroupBo bo);

}
