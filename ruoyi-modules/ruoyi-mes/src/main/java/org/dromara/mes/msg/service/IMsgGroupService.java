package org.dromara.mes.msg.service;

import org.dromara.mes.msg.domain.vo.MsgGroupCodeVo;
import org.dromara.mes.msg.domain.vo.MsgGroupVo;
import org.dromara.mes.msg.domain.bo.MsgGroupBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 分组信息Service接口
 *
 * @author zhout
 * @date 2025-06-08
 */
public interface IMsgGroupService {

    /**
     * 查询分组信息
     *
     * @param id 主键
     * @return 分组信息
     */
    MsgGroupVo queryById(Long id);

    /**
     * 分页查询分组信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 分组信息分页列表
     */
    TableDataInfo<MsgGroupVo> queryPageList(MsgGroupBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的分组信息列表
     *
     * @param bo 查询条件
     * @return 分组信息列表
     */
    List<MsgGroupVo> queryList(MsgGroupBo bo);

    /**
     * 新增分组信息
     *
     * @param bo 分组信息
     * @return 是否新增成功
     */
    Boolean insertByBo(MsgGroupBo bo);

    /**
     * 修改分组信息
     *
     * @param bo 分组信息
     * @return 是否修改成功
     */
    Boolean updateByBo(MsgGroupBo bo);

    /**
     * 校验并批量删除分组信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询分组信息列表
     *
     * @param groupName 分组名称
     * @param id        主键
     * @param pageQuery 分页参数
     * @return 分组信息列表
     */
    List<MsgGroupCodeVo> queryGroupCodePageList(String groupName, String id, PageQuery pageQuery);
}
