package org.dromara.mes.msg.service;

import org.dromara.mes.msg.domain.vo.MsgUserGroupVo;
import org.dromara.mes.msg.domain.bo.MsgUserGroupBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户组Service接口
 *
 * @author zhout
 * @date 2025-06-08
 */
public interface IMsgUserGroupService {

    /**
     * 查询用户组
     *
     * @param id 主键
     * @return 用户组
     */
    MsgUserGroupVo queryById(Long id);

    /**
     * 分页查询用户组列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户组分页列表
     */
    TableDataInfo<MsgUserGroupVo> queryPageList(MsgUserGroupBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的用户组列表
     *
     * @param bo 查询条件
     * @return 用户组列表
     */
    List<MsgUserGroupVo> queryList(MsgUserGroupBo bo);

    /**
     * 新增用户组
     *
     * @param bo 用户组
     * @return 是否新增成功
     */
    Boolean insertByBo(MsgUserGroupBo bo);

    /**
     * 修改用户组
     *
     * @param bo 用户组
     * @return 是否修改成功
     */
    Boolean updateByBo(MsgUserGroupBo bo);

    /**
     * 校验并批量删除用户组信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
