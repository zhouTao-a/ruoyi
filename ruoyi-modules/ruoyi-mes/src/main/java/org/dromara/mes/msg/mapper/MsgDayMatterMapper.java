package org.dromara.mes.msg.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.mes.msg.domain.bo.MsgDayMatterBo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 事件Mapper接口
 *
 * @author zhout
 * @date 2025-06-08
 */
public interface MsgDayMatterMapper extends BaseMapperPlus<MsgDayMatter, MsgDayMatterVo> {

    /**
     * 分页查询
     *
     * @param build 分页条件
     * @param bo    查询条件
     * @return 事件列表
     */
    Page<MsgDayMatterVo> queryPageList(Page<Object> build,
                                       @Param("param") MsgDayMatterBo bo);
}
