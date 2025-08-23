package org.dromara.mes.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.mes.msg.domain.MsgGroup;
import org.dromara.mes.msg.domain.vo.MsgGroupCodeVo;
import org.dromara.mes.msg.domain.vo.MsgGroupVo;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgGroupBo;
import org.dromara.mes.msg.mapper.MsgGroupMapper;
import org.dromara.mes.msg.service.IMsgGroupService;

import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * 分组信息Service业务层处理
 *
 * @author zhout
 * @date 2025-06-08
 */
@RequiredArgsConstructor
@Service
public class MsgGroupServiceImpl implements IMsgGroupService {

    private final MsgGroupMapper baseMapper;

    /**
     * 查询分组信息
     *
     * @param id 主键
     * @return 分组信息
     */
    @Override
    public MsgGroupVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询分组信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 分组信息分页列表
     */
    @Override
    public TableDataInfo<MsgGroupVo> queryPageList(MsgGroupBo bo, PageQuery pageQuery) {
        Page<MsgGroupVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的分组信息列表
     *
     * @param bo 查询条件
     * @return 分组信息列表
     */
    @Override
    public List<MsgGroupVo> queryList(MsgGroupBo bo) {
        Page<Object> build = new PageQuery().build();
        Page<MsgGroupVo> msgGroupVoPage = baseMapper.queryPageList(build, bo);
        return msgGroupVoPage.getRecords();
    }

    /**
     * 新增分组信息
     *
     * @param bo 分组信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgGroupBo bo) {
        validEntityBeforeSave(bo);
        MsgGroup add = MapstructUtils.convert(bo, MsgGroup.class);
        assert add != null;
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改分组信息
     *
     * @param bo 分组信息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgGroupBo bo) {
        validEntityBeforeSave(bo);
        LambdaUpdateWrapper<MsgGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MsgGroup::getId, bo.getId())
            .set(MsgGroup::getGroupName, bo.getGroupName())
            .set(MsgGroup::getGroupCode, bo.getGroupCode())
            .set(MsgGroup::getDefaultTargetUserId, bo.getDefaultTargetUserId())
            .set(MsgGroup::getUpdateTime, new Date());
        return baseMapper.update(updateWrapper) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgGroupBo entity){
        boolean exists = baseMapper.exists(Wrappers.<MsgGroup>lambdaQuery()
            .eq(MsgGroup::getGroupCode, entity.getGroupCode())
            .ne(entity.getId() != null, MsgGroup::getId, entity.getId()));
        if (exists) {
            throw new ServiceException("分组代码不能重复!");
        }
    }

    /**
     * 校验并批量删除分组信息信息
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
    public List<MsgGroupCodeVo> queryGroupCodePageList(String groupName, String id, PageQuery pageQuery) {
        if (id != null) {
            groupName = null;
        }
        return baseMapper.queryGroupCodePageList(pageQuery.build(), groupName, id);
    }
}
