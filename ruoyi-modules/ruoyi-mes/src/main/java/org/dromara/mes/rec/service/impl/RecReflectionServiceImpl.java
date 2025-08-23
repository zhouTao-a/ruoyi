package org.dromara.mes.rec.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;
import org.dromara.mes.rec.domain.bo.RecReflectionBo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.RecReflection;
import org.dromara.mes.rec.mapper.RecReflectionMapper;
import org.dromara.mes.rec.service.IRecReflectionService;

import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * 感想Service业务层处理
 *
 * @author allen
 * @date 2025-08-09
 */
@RequiredArgsConstructor
@Service
public class RecReflectionServiceImpl implements IRecReflectionService {

    private final RecReflectionMapper baseMapper;

    /**
     * 查询感想
     *
     * @param id 主键
     * @return 感想
     */
    @Override
    public RecReflectionVo queryById(Long id){
        RecReflectionBo bo = new RecReflectionBo();
        bo.setId(id);
        return queryPageList(bo, new PageQuery(1, 1)).getRows().get(0);
    }

    /**
     * 分页查询感想列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 感想分页列表
     */
    @Override
    public TableDataInfo<RecReflectionVo> queryPageList(RecReflectionBo bo, PageQuery pageQuery) {
        bo.setUserId(LoginHelper.getUserId());
        Page<RecReflectionVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的感想列表
     *
     * @param bo 查询条件
     * @return 感想列表
     */
    @Override
    public List<RecReflectionVo> queryList(RecReflectionBo bo) {
        return queryPageList(bo, new PageQuery()).getRows();
    }

    /**
     * 新增感想
     *
     * @param bo 感想
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(RecReflectionBo bo) {
        RecReflection add = MapstructUtils.convert(bo, RecReflection.class);
        assert add != null;
        add.setUserId(LoginHelper.getUserId());
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改感想
     *
     * @param bo 感想
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(RecReflectionBo bo) {
        RecReflection update = MapstructUtils.convert(bo, RecReflection.class);
        assert update != null;
        validEntityBeforeSave(update);
        LambdaUpdateWrapper<RecReflection> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RecReflection::getId, update.getId())
            .set(RecReflection::getTitle, update.getTitle())
            .set(RecReflection::getSynopsis, update.getSynopsis())
            .set(RecReflection::getContent, update.getContent())
            .set(RecReflection::getSourceType, update.getSourceType())
            .set(RecReflection::getSourceName, update.getSourceName())
            .set(RecReflection::getSourceLink, update.getSourceLink())
            .set(RecReflection::getUpdateTime, new Date());
        return baseMapper.update(updateWrapper) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RecReflection entity){
        boolean exists = baseMapper.exists(Wrappers.<RecReflection>lambdaQuery()
            .eq(RecReflection::getTitle, entity.getTitle())
            .eq(RecReflection::getUserId, entity.getUserId())
            .ne(entity.getId() != null, RecReflection::getId, entity.getId()));
        if (exists) {
            throw new ServiceException("感想已存在!");
        }
    }

    /**
     * 校验并批量删除感想信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
