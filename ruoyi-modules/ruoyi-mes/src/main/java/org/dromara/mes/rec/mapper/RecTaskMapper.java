package org.dromara.mes.rec.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.rec.domain.RecTask;
import org.dromara.mes.rec.domain.bo.RecTaskBo;
import org.dromara.mes.rec.domain.vo.RecTaskVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 任务Mapper接口
 *
 * @author allen
 * @date 2025-08-09
 */
@Mapper
public interface RecTaskMapper extends BaseMapperPlus<RecTask, RecTaskVo> {

    /**
     * 查询列表
     *
     * @param bo 查询参数
     * @return 查询结果
     */
    Page<RecTaskVo> queryPageList(Page<Object> build,
                                  @Param("param") RecTaskBo bo);
}
