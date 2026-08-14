package org.dromara.mes.msg.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.msg.domain.MsgDayMatter;
import org.dromara.mes.msg.domain.bo.MsgDayMatterBo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterNameVo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 事件Mapper接口
 *
 * @author zhout
 * @date 2025-06-08
 */
@Mapper
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

    /**
     * 查询指定名称的列表
     *
     * @param build 分页条件
     * @param dayName 名称
     * @param id id
     * @return 事件列表
     */
    List<MsgDayMatterNameVo> queryDayNameList(Page<Object> build,
                                              @Param("dayName") String dayName,
                                              @Param("id") String id);

    /**
     * 最近一条未删除事件（按创建时间倒序，用于试发邮件）
     */
    MsgDayMatterVo selectLatestOne();
}
