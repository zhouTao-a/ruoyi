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

    /**
     * 插入数据
     */
    void insertIntoTestDataNoTransaction(long l);

    /**
     * 更新数据
     */
    void updateTestData(long id);

    /**
     * 更新数据
     */
    void updateTestDataNoTransaction(long id);

    /**
     * 更新数据
     */
    void updateTestDataRange(long start, long end);

    /**
     * 删除数据
     */
    void deleteTestDataRange(long start, long end);
}
