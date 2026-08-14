package org.dromara.mes.rec.service;

import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.dromara.mes.rec.domain.bo.RecTaskBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 任务Service接口
 *
 * @author allen
 * @date 2025-08-09
 */
public interface IRecTaskService {

    /**
     * 查询任务
     *
     * @param id 主键
     * @return 任务
     */
    RecTaskVo queryById(Long id);

    /**
     * 分页查询任务列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 任务分页列表
     */
    TableDataInfo<RecTaskVo> queryPageList(RecTaskBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的任务列表
     *
     * @param bo 查询条件
     * @return 任务列表
     */
    List<RecTaskVo> queryList(RecTaskBo bo);

    /**
     * 新增任务
     *
     * @param bo 任务
     * @return 是否新增成功
     */
    Boolean insertByBo(RecTaskBo bo);

    /**
     * 修改任务
     *
     * @param bo 任务
     * @return 是否修改成功
     */
    Boolean updateByBo(RecTaskBo bo);

    /**
     * 校验并批量删除任务信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 批量修改状态（仅当前登录用户的数据）
     */
    Boolean updateStatusByIds(List<Long> ids, String status);
}
