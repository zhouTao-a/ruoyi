package org.dromara.mes.rec.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mes.rec.domain.RecGoal;
import org.dromara.mes.rec.domain.vo.RecGoalVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 目标Mapper接口
 *
 * @author allen
 * @date 2025-08-09
 */
@Mapper
public interface RecGoalMapper extends BaseMapperPlus<RecGoal, RecGoalVo> {

}
