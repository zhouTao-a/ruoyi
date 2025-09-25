package org.dromara.mes.rec.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mes.rec.domain.RecReflection;
import org.dromara.mes.rec.domain.bo.RecReflectionBo;
import org.dromara.mes.rec.domain.vo.RecReflectionVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 感想Mapper接口
 *
 * @author allen
 * @date 2025-08-09
 */
@Mapper
public interface RecReflectionMapper extends BaseMapperPlus<RecReflection, RecReflectionVo> {

    /**
     * 查询列表
     *
     * @param param 查询参数
     * @return 列表
     */
    Page<RecReflectionVo> queryPageList(Page<Object> build,
                                        @Param("param") RecReflectionBo param);

    /**
     * 幻读测试 select ... for update
     */
    List<RecReflection> selectForUpdate();

    /**
     * 删除数据
     *
     * @param id 主键
     */
    void deleteSqlById(Long id);

    /**
     * 幻读测试 select ... LOCK IN SHARE MODE/ FOR SHARE
     */
    List<RecReflection> selectForShare();
}
