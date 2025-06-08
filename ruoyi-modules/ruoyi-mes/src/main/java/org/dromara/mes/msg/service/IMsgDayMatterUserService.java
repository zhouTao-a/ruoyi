package org.dromara.mes.msg.service;

import org.dromara.mes.msg.domain.vo.MsgDayMatterUserVo;
import org.dromara.mes.msg.domain.bo.MsgDayMatterUserBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 事件与用户关联Service接口
 *
 * @author zhout
 * @date 2025-06-08
 */
public interface IMsgDayMatterUserService {

    /**
     * 查询事件与用户关联
     *
     * @param id 主键
     * @return 事件与用户关联
     */
    MsgDayMatterUserVo queryById(Long id);

    /**
     * 分页查询事件与用户关联列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 事件与用户关联分页列表
     */
    TableDataInfo<MsgDayMatterUserVo> queryPageList(MsgDayMatterUserBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的事件与用户关联列表
     *
     * @param bo 查询条件
     * @return 事件与用户关联列表
     */
    List<MsgDayMatterUserVo> queryList(MsgDayMatterUserBo bo);

    /**
     * 新增事件与用户关联
     *
     * @param bo 事件与用户关联
     * @return 是否新增成功
     */
    Boolean insertByBo(MsgDayMatterUserBo bo);

    /**
     * 修改事件与用户关联
     *
     * @param bo 事件与用户关联
     * @return 是否修改成功
     */
    Boolean updateByBo(MsgDayMatterUserBo bo);

    /**
     * 校验并批量删除事件与用户关联信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
