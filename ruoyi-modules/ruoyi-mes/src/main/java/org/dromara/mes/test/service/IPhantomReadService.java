package org.dromara.mes.test.service;

import org.dromara.mes.rec.domain.RecReflection;

import java.util.List;

public interface IPhantomReadService {
    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    List<RecReflection> selectList();

    /**
     * 插入数据
     */
    void insertIntoTestData(long l);
}
