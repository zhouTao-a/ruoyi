package org.dromara.mes.rec.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;
import org.dromara.mes.rec.domain.bo.RecTaskBo;
import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.dromara.mes.rec.domain.RecTask;
import org.dromara.mes.rec.mapper.RecTaskMapper;
import org.dromara.mes.rec.service.IRecTaskService;

import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * 任务Service业务层处理
 *
 * @author allen
 * @date 2025-08-09
 */
@RequiredArgsConstructor
@Service
public class RecTaskServiceImpl implements IRecTaskService {

    private final RecTaskMapper baseMapper;

    /**
     * 查询任务
     *
     * @param id 主键
     * @return 任务
     */
    @Override
    public RecTaskVo queryById(Long id){
        RecTaskBo bo = new RecTaskBo();
        bo.setId(id);
        return queryPageList(bo, new PageQuery(1, 1)).getRows().get(0);
    }

    /**
     * 分页查询任务列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 任务分页列表
     */
    @Override
    public TableDataInfo<RecTaskVo> queryPageList(RecTaskBo bo, PageQuery pageQuery) {
        //只查询当前登录人的任务
        bo.setUserId(LoginHelper.getUserId());
        if (bo.getEndDeadLine() != null) {
            bo.setEndDeadLine(DateUtils.addDays(bo.getEndDeadLine(), 1));
        }
        Page<RecTaskVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的任务列表
     *
     * @param bo 查询条件
     * @return 任务列表
     */
    @Override
    public List<RecTaskVo> queryList(RecTaskBo bo) {
        return queryPageList(bo, new PageQuery()).getRows();
    }

    /**
     * 新增任务
     *
     * @param bo 任务
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(RecTaskBo bo) {
        RecTask add = MapstructUtils.convert(bo, RecTask.class);
        assert add != null;
        add.setUserId(LoginHelper.getUserId());
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改任务
     *
     * @param bo 任务
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(RecTaskBo bo) {
        RecTask update = MapstructUtils.convert(bo, RecTask.class);
        assert update != null;
        validEntityBeforeSave(update);
        LambdaUpdateWrapper<RecTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RecTask::getId, update.getId())
            .set(RecTask::getTitle, update.getTitle())
            .set(RecTask::getContent, update.getContent())
            .set(RecTask::getStatus, update.getStatus())
            .set(RecTask::getProgress, update.getProgress())
            .set(RecTask::getDeadLine, update.getDeadLine())
            .set(RecTask::getPriority, update.getPriority())
            .set(RecTask::getUpdateTime, new Date());
        return baseMapper.update(updateWrapper) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RecTask entity){
        boolean exists = baseMapper.exists(Wrappers.<RecTask>lambdaQuery()
            .eq(RecTask::getTitle, entity.getTitle())
            .eq(RecTask::getUserId, entity.getUserId())
            .ne(entity.getId() != null, RecTask::getId, entity.getId()));
        if (exists) {
            throw new ServiceException("任务已存在!");
        }
    }

    /**
     * 校验并批量删除任务信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public Boolean updateStatusByIds(List<Long> ids, String status) {
        LambdaUpdateWrapper<RecTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(RecTask::getId, ids)
            .eq(RecTask::getUserId, LoginHelper.getUserId())
            .set(RecTask::getStatus, status)
            .set(RecTask::getUpdateTime, new Date());
        if ("completed".equals(status)) {
            updateWrapper.set(RecTask::getProgress, 100L);
        }
        return baseMapper.update(updateWrapper) > 0;
    }
}
