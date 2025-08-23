package org.dromara.mes.msg.service;

import org.dromara.mes.msg.domain.vo.MsgDayMatterNameVo;
import org.dromara.mes.msg.domain.vo.MsgDayMatterVo;
import org.dromara.mes.msg.domain.bo.MsgDayMatterBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.mes.msg.domain.vo.ReminderVo;

import java.util.Collection;
import java.util.List;

/**
 * 事件Service接口
 *
 * @author zhout
 * @date 2025-06-08
 */
public interface IMsgDayMatterService {

    /**
     * 查询事件
     *
     * @param id 主键
     * @return 事件
     */
    MsgDayMatterVo queryById(Long id);

    /**
     * 分页查询事件列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 事件分页列表
     */
    TableDataInfo<MsgDayMatterVo> queryPageList(MsgDayMatterBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的事件列表
     *
     * @param bo 查询条件
     * @return 事件列表
     */
    List<MsgDayMatterVo> queryList(MsgDayMatterBo bo);

    /**
     * 新增事件
     *
     * @param bo 事件
     * @return 是否新增成功
     */
    Boolean insertByBo(MsgDayMatterBo bo);

    /**
     * 修改事件
     *
     * @param bo 事件
     * @return 是否修改成功
     */
    Boolean updateByBo(MsgDayMatterBo bo);

    /**
     * 校验并批量删除事件信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询事件名称列表
     *
     * @param dayName 事件名称
     * @param id      主键
     * @return 事件名称列表
     */
    List<MsgDayMatterNameVo> queryDayNameList(String dayName, String id, PageQuery pageQuery);

    /**
     * 查询事件列表
     *
     * @param year    年
     * @param month   月
     * @param groupId 分组id
     * @return 事件列表
     */
    List<ReminderVo> dayMatterList(int year, int month, Long groupId);

    /**
     * 定时更新下次提醒时间
     */
    void updateNextNotifyTime();
}
