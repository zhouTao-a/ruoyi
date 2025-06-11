package org.dromara.mes.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.mes.msg.domain.bo.MsgUserBo;
import org.dromara.mes.msg.domain.vo.MsgUserVo;
import org.dromara.mes.msg.domain.MsgUser;
import org.dromara.mes.msg.mapper.MsgUserMapper;
import org.dromara.mes.msg.service.IMsgUserService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Collection;

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
        Map<String, Object> params = bo.getParams();
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
        UpdateWrapper<MsgUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", bo.getId())
            .set("birthday", bo.getBirthday())
            .set("email", bo.getEmail())
            .set("email_notify_flag", bo.getEmailNotifyFlag())
            .set("gender", bo.getGender())
            .set("id_card", bo.getIdCard())
            .set("lunar_birthday", bo.getLunarBirthday())
            .set("phone_number", bo.getPhoneNumber())
            .set("sms_notify_flag", bo.getSmsNotifyFlag())
            .set("user_code", bo.getUserCode())
            .set("user_name", bo.getUserName())
            .set("update_time", new Date());
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MsgUser entity){
        //TODO 做一些数据校验,如唯一约束
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
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
