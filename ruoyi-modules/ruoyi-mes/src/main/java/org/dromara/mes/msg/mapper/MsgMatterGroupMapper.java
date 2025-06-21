package org.dromara.mes.msg.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.msg.domain.MsgMatterGroup;
import org.dromara.mes.msg.domain.bo.MsgMatterGroupBo;
import org.dromara.mes.msg.domain.vo.MsgMatterGroupVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 事件组Mapper接口
 *
 * @author allen
 * @date 2025-06-18
 */
@Mapper
public interface MsgMatterGroupMapper extends BaseMapperPlus<MsgMatterGroup, MsgMatterGroupVo> {

    /**
     * 分页查询
     *
     * @param build 分页条件
     * @param bo    查询条件
     * @return      事件组
     */
    Page<MsgMatterGroupVo> queryPageList(Page<Object> build,
                                         @Param("param") MsgMatterGroupBo bo);
}
