package org.dromara.mes.rec.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.mes.rec.domain.bo.RecReflectionBo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.mes.rec.domain.RecReflection;
import org.dromara.mes.rec.mapper.RecReflectionMapper;
import org.dromara.mes.rec.service.IRecReflectionService;

import java.util.List;
import java.util.Map;
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
        return baseMapper.selectVoById(id);
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
        LambdaQueryWrapper<RecReflection> lqw = buildQueryWrapper(bo);
        Page<RecReflectionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
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
        LambdaQueryWrapper<RecReflection> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<RecReflection> buildQueryWrapper(RecReflectionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<RecReflection> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(RecReflection::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), RecReflection::getTitle, bo.getTitle());
        return lqw;
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
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
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
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RecReflection entity){
        //TODO 做一些数据校验,如唯一约束
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
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
