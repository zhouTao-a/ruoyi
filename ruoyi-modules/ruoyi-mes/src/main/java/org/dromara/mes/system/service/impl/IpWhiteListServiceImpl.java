package org.dromara.mes.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.mes.system.domain.IpWhiteList;
import org.dromara.mes.system.domain.bo.IpWhiteListBo;
import org.dromara.mes.system.domain.vo.IpWhiteListVo;
import org.dromara.mes.system.mapper.IpWhiteListMapper;
import org.dromara.mes.system.service.IIpWhiteListService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * IP白名单Service业务层处理
 *
 * @author Lion Li
 * @date 2025-05-22
 */
@RequiredArgsConstructor
@Service
public class IpWhiteListServiceImpl implements IIpWhiteListService {

    private final IpWhiteListMapper baseMapper;

    /**
     * 查询IP白名单
     *
     * @param id 主键
     * @return IP白名单
     */
    @Override
    public IpWhiteListVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询IP白名单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return IP白名单分页列表
     */
    @Override
    public TableDataInfo<IpWhiteListVo> queryPageList(IpWhiteListBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<IpWhiteList> lqw = buildQueryWrapper(bo);
        Page<IpWhiteListVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的IP白名单列表
     *
     * @param bo 查询条件
     * @return IP白名单列表
     */
    @Override
    public List<IpWhiteListVo> queryList(IpWhiteListBo bo) {
        LambdaQueryWrapper<IpWhiteList> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<IpWhiteList> buildQueryWrapper(IpWhiteListBo bo) {
        LambdaQueryWrapper<IpWhiteList> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(IpWhiteList::getUpdateTime);
        lqw.eq(StringUtils.isNotBlank(bo.getIpAddress()), IpWhiteList::getIpAddress, bo.getIpAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), IpWhiteList::getDescription, bo.getDescription());
        lqw.eq(bo.getStatus() != null, IpWhiteList::getStatus, bo.getStatus());
        lqw.eq(bo.getDeptId() != null, IpWhiteList::getDeptId, bo.getDeptId());
        return lqw;
    }

    /**
     * 新增IP白名单
     *
     * @param bo IP白名单
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(IpWhiteListBo bo) {
        IpWhiteList add = MapstructUtils.convert(bo, IpWhiteList.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改IP白名单
     *
     * @param bo IP白名单
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(IpWhiteListBo bo) {
        IpWhiteList update = MapstructUtils.convert(bo, IpWhiteList.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(IpWhiteList entity){

    }

    /**
     * 校验并批量删除IP白名单信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){

        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
