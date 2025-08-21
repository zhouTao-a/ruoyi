package org.dromara.mes.rec.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;
import org.dromara.mes.rec.domain.bo.RecGoalBo;
import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.mes.rec.domain.RecGoal;
import org.dromara.mes.rec.mapper.RecGoalMapper;
import org.dromara.mes.rec.service.IRecGoalService;

import java.util.Date;
import java.util.List;

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
    public TableDataInfo<RecGoalVo> queryPageList(RecGoalBo bo, PageQuery pageQuery) {
        bo.setUserId(LoginHelper.getUserId());
        bo.setLevel(1);
        Page<RecGoalVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
        TableDataInfo<RecGoalVo> build = TableDataInfo.build(result);
        List<RecGoalVo> records = result.getRecords();
        // 获取子目标
        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> list = records.stream().map(RecGoalVo::getId).toList();
            bo = new RecGoalBo();
            bo.setUserId(LoginHelper.getUserId());
            bo.setTopIds(list);
            result = baseMapper.queryPageList(new PageQuery().build(), bo);
            build.getRows().addAll(result.getRecords());
        }
        return build;
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
        assert add != null;
        add.setUserId(LoginHelper.getUserId());
        // 设置父级目标
        setParentAndTopId(add);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    private void setParentAndTopId(RecGoal add) {
        if (add.getParentId() != 0) {
            RecGoalVo recGoalVo = baseMapper.selectVoById(add.getParentId());
            add.setLevel(recGoalVo.getLevel() + 1);
            if (recGoalVo.getTopId() != 0) {
                add.setTopId(recGoalVo.getTopId());
            } else {
                add.setTopId(recGoalVo.getId());
            }
        }
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
        assert update != null;
        validEntityBeforeSave(update);
        // 设置父级目标
        setParentAndTopId(update);
        LambdaUpdateWrapper<RecGoal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RecGoal::getId, update.getId())
            .set(RecGoal::getTitle, update.getTitle())
            .set(RecGoal::getContent, update.getContent())
            .set(RecGoal::getLevel, update.getLevel())
            .set(RecGoal::getTopId, update.getTopId())
            .set(RecGoal::getUserId, update.getUserId())
            .set(RecGoal::getProgress, update.getProgress())
            .set(RecGoal::getStatus, update.getStatus())
            .set(RecGoal::getParentId, update.getParentId())
            .set(RecGoal::getSortOrder, update.getSortOrder())
            .set(RecGoal::getDeadLine, update.getDeadLine())
            .set(RecGoal::getUpdateTime, new Date());
        return baseMapper.update(updateWrapper) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RecGoal entity){
        boolean exists = baseMapper.exists(Wrappers.<RecGoal>lambdaQuery()
            .eq(RecGoal::getTitle, entity.getTitle())
            .eq(RecGoal::getUserId, entity.getUserId())
            .eq(RecGoal::getLevel, entity.getLevel())
            .ne(entity.getId() != null, RecGoal::getId, entity.getId()));
        if (exists) {
            throw new ServiceException("目标已存在!");
        }
    }

    /**
     * 校验并批量删除目标信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid) {
        if (isValid) {
            boolean exists = baseMapper.exists(Wrappers.<RecGoal>lambdaQuery().eq(RecGoal::getParentId, ids.get(0))
                .eq(RecGoal::getUserId, LoginHelper.getUserId()));
            if (exists) {
                throw new ServiceException("目标下有子目标，请先删除子目标");
            }
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
