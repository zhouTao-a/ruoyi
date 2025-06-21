package org.dromara.mes.msg.service;

import org.dromara.mes.msg.domain.vo.MsgUserCodeVo;
import org.dromara.mes.msg.domain.vo.MsgUserVo;
import org.dromara.mes.msg.domain.bo.MsgUserBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户Service接口
 *
 * @author zhout
 * @date 2025-06-08
 */
public interface IMsgUserService {

    /**
     * 查询用户
     *
     * @param id 主键
     * @return 用户
     */
    MsgUserVo queryById(Long id);

    /**
     * 分页查询用户列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户分页列表
     */
    TableDataInfo<MsgUserVo> queryPageList(MsgUserBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的用户列表
     *
     * @param bo 查询条件
     * @return 用户列表
     */
    List<MsgUserVo> queryList(MsgUserBo bo);

    /**
     * 新增用户
     *
     * @param bo 用户
     * @return 是否新增成功
     */
    Boolean insertByBo(MsgUserBo bo);

    /**
     * 修改用户
     *
     * @param bo 用户
     * @return 是否修改成功
     */
    Boolean updateByBo(MsgUserBo bo);

    /**
     * 校验并批量删除用户信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询用户代码列表
     *
     * @param userName  查询条件
     * @param id        ID
     * @param pageQuery 分页参数
     * @return 用户代码分页列表
     */
    List<MsgUserCodeVo> queryUserCodePageList(String userName, String id, PageQuery pageQuery);
}
