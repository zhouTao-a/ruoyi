package org.dromara.mes.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgUserGroupBo;
import org.dromara.mes.msg.domain.vo.MsgUserGroupVo;
import org.dromara.mes.msg.domain.MsgUserGroup;
import org.dromara.mes.msg.mapper.MsgUserGroupMapper;
import org.dromara.mes.msg.service.IMsgUserGroupService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * 用户组Service业务层处理
 *
 * @author zhout
 * @date 2025-06-08
 */
@RequiredArgsConstructor
@Service
public class MsgUserGroupServiceImpl implements IMsgUserGroupService {

    private final MsgUserGroupMapper baseMapper;

    /**
     * 查询用户组
     *
     * @param id 主键
     * @return 用户组
     */
    @Override
    public MsgUserGroupVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询用户组列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户组分页列表
     */
    @Override
    public TableDataInfo<MsgUserGroupVo> queryPageList(MsgUserGroupBo bo, PageQuery pageQuery) {
        Page<MsgUserGroupVo> result = baseMapper.queryPageList(pageQuery.build(), bo);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的用户组列表
     *
     * @param bo 查询条件
     * @return 用户组列表
     */
    @Override
    public List<MsgUserGroupVo> queryList(MsgUserGroupBo bo) {
        Page<MsgUserGroupVo> result = baseMapper.queryPageList(new PageQuery().build(), bo);
        return result.getRecords();
    }

    /**
     * 新增用户组
     *
     * @param bo 用户组
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgUserGroupBo bo) {
        List<String> userIdList = bo.getUserIdList();
        List<MsgUserGroup> msgUserGroupList = new ArrayList<>();
        if (userIdList != null && !userIdList.isEmpty()) {
            for (String userId : userIdList) {
                MsgUserGroup msgUserGroup = new MsgUserGroup();
                msgUserGroup.setUserId(Long.parseLong(userId));
                msgUserGroup.setGroupId(bo.getGroupId());
                msgUserGroup.setRelativeGenerationDiff(bo.getRelativeGenerationDiff());
                msgUserGroup.setKinshipLevel(bo.getKinshipLevel());
                if (validEntityBeforeSave(msgUserGroup)) {
                    msgUserGroupList.add(msgUserGroup);
                }
            }
        }
        return msgUserGroupList.isEmpty() || baseMapper.insertBatch(msgUserGroupList);
    }

    /**
     * 修改用户组
     *
     * @param bo 用户组
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgUserGroupBo bo) {
        List<String> userIdList = bo.getUserIdList();
        if (userIdList != null && !userIdList.isEmpty()) {
            for (String userId : userIdList) {
                MsgUserGroup msgUserGroup = new MsgUserGroup();
                msgUserGroup.setId(bo.getId());
                msgUserGroup.setUserId(Long.parseLong(userId));
                msgUserGroup.setGroupId(bo.getGroupId());
                msgUserGroup.setRelativeGenerationDiff(bo.getRelativeGenerationDiff());
                msgUserGroup.setKinshipLevel(bo.getKinshipLevel());
                if (validEntityBeforeSave(msgUserGroup)) {
                    LambdaUpdateWrapper<MsgUserGroup> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(MsgUserGroup::getId, bo.getId());
                    updateWrapper.set(MsgUserGroup::getUserId, Long.parseLong(userId));
                    updateWrapper.set(MsgUserGroup::getGroupId, bo.getGroupId());
                    updateWrapper.set(MsgUserGroup::getRelativeGenerationDiff, bo.getRelativeGenerationDiff());
                    updateWrapper.set(MsgUserGroup::getKinshipLevel, bo.getKinshipLevel());
                    updateWrapper.set(MsgUserGroup::getUpdateTime, new Date());
                    baseMapper.update(updateWrapper);
                }
            }
        }
        return true;
    }

    /**
     * 保存前的数据校验
     */
    private boolean validEntityBeforeSave(MsgUserGroup entity){
        List<MsgUserGroupVo> msgUserGroupList = baseMapper.selectVoList(new LambdaQueryWrapper<MsgUserGroup>()
            .eq(MsgUserGroup::getUserId, entity.getUserId())
            .eq(MsgUserGroup::getGroupId, entity.getGroupId())
            .ne(entity.getId() != null, MsgUserGroup::getId, entity.getId()));
        return msgUserGroupList.isEmpty();
    }

    /**
     * 校验并批量删除用户组信息
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
