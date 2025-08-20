package org.dromara.mes.rec.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.rec.domain.RecGoal;
import org.dromara.mes.rec.domain.bo.RecGoalBo;
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

    /**
     * 分页查询
     *
     * @param build  分页参数
     * @param bo  查询条件
     * @return 结果
     */
    Page<RecGoalVo> queryPageList(Page<Object> build,
                                  @Param("param") RecGoalBo bo);
}
