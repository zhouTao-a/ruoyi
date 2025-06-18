package org.dromara.mes.msg.service;

import org.dromara.mes.msg.domain.vo.MsgMatterGroupVo;
import org.dromara.mes.msg.domain.bo.MsgMatterGroupBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 事件组Service接口
 *
 * @author allen
 * @date 2025-06-18
 */
public interface IMsgMatterGroupService {

    /**
     * 查询事件组
     *
     * @param id 主键
     * @return 事件组
     */
    MsgMatterGroupVo queryById(Long id);

    /**
     * 分页查询事件组列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 事件组分页列表
     */
    TableDataInfo<MsgMatterGroupVo> queryPageList(MsgMatterGroupBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的事件组列表
     *
     * @param bo 查询条件
     * @return 事件组列表
     */
    List<MsgMatterGroupVo> queryList(MsgMatterGroupBo bo);

    /**
     * 新增事件组
     *
     * @param bo 事件组
     * @return 是否新增成功
     */
    Boolean insertByBo(MsgMatterGroupBo bo);

    /**
     * 修改事件组
     *
     * @param bo 事件组
     * @return 是否修改成功
     */
    Boolean updateByBo(MsgMatterGroupBo bo);

    /**
     * 校验并批量删除事件组信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
