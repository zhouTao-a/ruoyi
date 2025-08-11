package org.dromara.mes.rec.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.mes.rec.domain.bo.RecGoalBo;
import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.domain.RecGoal;
import org.dromara.mes.rec.mapper.RecGoalMapper;
import org.dromara.mes.rec.service.IRecGoalService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 目标Service业务层处理
 *
 * @author allen
 * @date 2025-08-09
 */
@RequiredArgsConstructor
@Service
public class RecGoalServiceImpl implements IRecGoalService {

    private final RecGoalMapper baseMapper;

    /**
     * 查询目标
     *
     * @param id 主键
     * @return 目标
     */
    @Override
    public RecGoalVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询符合条件的目标列表
     *
     * @param bo 查询条件
     * @return 目标列表
     */
    @Override
    public List<RecGoalVo> queryList(RecGoalBo bo) {
        LambdaQueryWrapper<RecGoal> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<RecGoal> buildQueryWrapper(RecGoalBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<RecGoal> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(RecGoal::getId);
        lqw.eq(bo.getParentId() != null, RecGoal::getParentId, bo.getParentId());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), RecGoal::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), RecGoal::getStatus, bo.getStatus());
        lqw.between(params.get("beginDeadline") != null && params.get("endDeadline") != null,
            RecGoal::getDeadLine,params.get("beginDeadline"), params.get("endDeadline"));
        return lqw;
    }

    /**
     * 新增目标
     *
     * @param bo 目标
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(RecGoalBo bo) {
        RecGoal add = MapstructUtils.convert(bo, RecGoal.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改目标
     *
     * @param bo 目标
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(RecGoalBo bo) {
        RecGoal update = MapstructUtils.convert(bo, RecGoal.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RecGoal entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除目标信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
