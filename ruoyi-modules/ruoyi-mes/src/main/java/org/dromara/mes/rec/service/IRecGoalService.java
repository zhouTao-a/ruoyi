package org.dromara.mes.rec.service;

import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.domain.bo.RecGoalBo;

import java.util.Collection;
import java.util.List;

/**
 * 目标Service接口
 *
 * @author allen
 * @date 2025-08-09
 */
public interface IRecGoalService {

    /**
     * 查询目标
     *
     * @param id 主键
     * @return 目标
     */
    RecGoalVo queryById(Long id);


    /**
     * 查询符合条件的目标列表
     *
     * @param bo 查询条件
     * @return 目标列表
     */
    List<RecGoalVo> queryList(RecGoalBo bo);

    /**
     * 新增目标
     *
     * @param bo 目标
     * @return 是否新增成功
     */
    Boolean insertByBo(RecGoalBo bo);

    /**
     * 修改目标
     *
     * @param bo 目标
     * @return 是否修改成功
     */
    Boolean updateByBo(RecGoalBo bo);

    /**
     * 校验并批量删除目标信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
