package org.dromara.mes.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dromara.mes.msg.domain.vo.MsgUserCodeVo;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgUserBo;
import org.dromara.mes.msg.domain.vo.MsgUserVo;
import org.dromara.mes.msg.domain.MsgUser;
import org.dromara.mes.msg.mapper.MsgUserMapper;
import org.dromara.mes.msg.service.IMsgUserService;

import java.util.*;

/**
 * 用户Service业务层处理
 *
 * @author zhout
 * @date 2025-06-08
 */
@RequiredArgsConstructor
@Service
public class MsgUserServiceImpl implements IMsgUserService {

    private final MsgUserMapper baseMapper;

    /**
     * 查询用户
     *
     * @param id 主键
     * @return 用户
     */
    @Override
    public MsgUserVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询用户列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户分页列表
     */
    @Override
    public TableDataInfo<MsgUserVo> queryPageList(MsgUserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MsgUser> lqw = buildQueryWrapper(bo);
        Page<MsgUserVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的用户列表
     *
     * @param bo 查询条件
     * @return 用户列表
     */
    @Override
    public List<MsgUserVo> queryList(MsgUserBo bo) {
        LambdaQueryWrapper<MsgUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MsgUser> buildQueryWrapper(MsgUserBo bo) {
        LambdaQueryWrapper<MsgUser> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(MsgUser::getUpdateTime);
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), MsgUser::getUserName, bo.getUserName());
        lqw.like(StringUtils.isNotBlank(bo.getPhoneNumber()), MsgUser::getPhoneNumber, bo.getPhoneNumber());
        return lqw;
    }

    /**
     * 新增用户
     *
     * @param bo 用户
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MsgUserBo bo) {
        MsgUser add = MapstructUtils.convert(bo, MsgUser.class);
        assert add != null;
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户
     *
     * @param bo 用户
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MsgUserBo bo) {
        LambdaUpdateWrapper<MsgUser> updateWrapper = new LambdaUpdateWrapper<>();
        List<MsgUserVo> msgUserVos = baseMapper.selectVoList(Wrappers.<MsgUser>lambdaQuery()
            .eq(MsgUser::getUserCode, bo.getUserCode())
            .ne(MsgUser::getId, bo.getId()));
        if (!msgUserVos.isEmpty()) {
            throw new ServiceException("用户代码不能重复!");
        }
        updateWrapper.eq(MsgUser::getId, bo.getId())
            .set(MsgUser::getBirthday, bo.getBirthday())
            .set(MsgUser::getEmail, bo.getEmail())
            .set(MsgUser::getEmailNotifyFlag, bo.getEmailNotifyFlag())
            .set(MsgUser::getGender, bo.getGender())
            .set(MsgUser::getIdCard, bo.getIdCard())
            .set(MsgUser::getLunarBirthday, bo.getLunarBirthday())
            .set(MsgUser::getPhoneNumber, bo.getPhoneNumber())
            .set(MsgUser::getSmsNotifyFlag, bo.getSmsNotifyFlag())
            .set(MsgUser::getUserCode, bo.getUserCode())
            .set(MsgUser::getUserName, bo.getUserName())
            .set(MsgUser::getUpdateTime, new Date());
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgUser entity){
        List<MsgUserVo> msgUserVos = baseMapper.selectVoList(Wrappers.<MsgUser>lambdaQuery()
            .eq(MsgUser::getUserCode, entity.getUserCode()));
        if (!msgUserVos.isEmpty()) {
            throw new ServiceException("用户代码不能重复!");
        }
    }

    /**
     * 校验并批量删除用户信息
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
    public List<MsgUserCodeVo> queryUserCodePageList(String userName, String id, PageQuery pageQuery) {
        if (StringUtils.isNotEmpty(id)) {
            userName = null;
        }
        Page<MsgUserCodeVo> result = baseMapper.queryUserCodePageList(pageQuery.build(), id, userName);
        return result.getRecords();
    }
}
