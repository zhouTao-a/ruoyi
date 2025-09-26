package org.dromara.mes.test.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.mes.rec.domain.RecReflection;
import org.dromara.mes.rec.service.IRecReflectionService;
import org.dromara.mes.test.service.IPhantomReadService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 幻读测试
 *
 * @author allen
 */
@RestController
@RequestMapping("/phantomRead")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PhantomReadController {

    private final IRecReflectionService iRecReflectionService;
    private final IPhantomReadService iPhantomReadService;

    /**
     * 幻读测试 select ... for update
     */
    @GetMapping("/selectForUpdate")
    public String selectForUpdate() {
        List<RecReflection> recReflections = iPhantomReadService.selectList();
        System.out.println("第一次快照读：" + recReflections.size());

        long id = 100L;
        iPhantomReadService.insertIntoTestData(id);

        System.out.println("插入数据后快照读：" + iPhantomReadService.selectList().size());
        List<RecReflection> selectForUpdates = iRecReflectionService.selectForUpdate();
        System.out.println("当前读 select ... for update 数据记录数：" + selectForUpdates.size());
        iRecReflectionService.deleteById(id);
        return "当前读 select ... for update 结束";
    }

    /**
     * 当前读 select ... for share
     */
    @GetMapping("/selectForShare")
    public String selectForShare() {
        List<RecReflection> recReflections = iPhantomReadService.selectList();
        System.out.println("第一次快照读：" + recReflections.size());

        long id = 100L;
        iPhantomReadService.insertIntoTestData(id);

        System.out.println("插入数据后快照读：" + iPhantomReadService.selectList().size());
        List<RecReflection> selectForUpdates = iRecReflectionService.selectForShare();
        System.out.println("当前读 select ... for share 数据记录数：" + selectForUpdates.size());
        iRecReflectionService.deleteById(id);
        //再次插入数据时，因为insertIntoTestData是新事务，而这个事物使用了共享读，不能插入数据，会阻塞
//        iPhantomReadService.insertIntoTestData();
        return "当前读 select ... for share 结束";
    }

    /**
     * 插入测试数据
     */
    @GetMapping("/insertIntoTestData")
    public void insertIntoTestData() {
        for (int i = 0; i < 5; i++) {
            iPhantomReadService.insertIntoTestData((i + 1) * 10L);
        }
    }

    /**
     * 更新测试数据
     */
    @GetMapping("/update")
    public void update() {
        List<RecReflection> recReflections = iPhantomReadService.selectList();
        System.out.println("第一次快照读：" + recReflections.size());

        long id = 20L;
        //精准修改会触发行锁，不会触发间隙锁
        iPhantomReadService.updateTestDataNoTransaction(id);

        System.out.println("插入数据后快照读：" + iPhantomReadService.selectList().size());
        iPhantomReadService.insertIntoTestData(15L);

        System.out.println("插入数据后快照读：" + iPhantomReadService.selectList().size());
    }

    /**
     * 范围更新
     */
    @GetMapping("/updateByRange")
    public void updateByRange() {
        List<RecReflection> recReflections = iPhantomReadService.selectList();
        System.out.println("第一次快照读：" + recReflections.size());

        iPhantomReadService.updateTestDataRange(10L, 20L);

        System.out.println("范围更新数据后快照读：" + iPhantomReadService.selectList().size());
        //范围更新时会触发间隙锁，导致新事务阻塞，插入失败
        iPhantomReadService.insertIntoTestData(15L);

        System.out.println("范围更新数据后快照读：" + iPhantomReadService.selectList().size());
    }

    /**
     * 范围删除
     */
    @GetMapping("/deleteByRange")
    public void deleteByRange() {
        List<RecReflection> recReflections = iPhantomReadService.selectList();
        System.out.println("第一次快照读：" + recReflections.size());

        iPhantomReadService.deleteTestDataRange(10L, 20L);

        System.out.println("删除数据后快照读：" + iPhantomReadService.selectList().size());
        //范围删除时会触发间隙锁，导致新事务阻塞，插入失败，删除数据也会回滚
        iPhantomReadService.insertIntoTestData(15L);

        System.out.println("删除数据后快照读：" + iPhantomReadService.selectList().size());
    }


    /**
     * 插入测试当前读
     */
    @GetMapping("/insert")
    public void insert() {
        List<RecReflection> recReflections = iPhantomReadService.selectList();
        System.out.println("第一次快照读：" + recReflections.size());

        iPhantomReadService.insertIntoTestDataNoTransaction(16L);

        System.out.println("插入数据后快照读：" + iPhantomReadService.selectList().size());
        //插入数据时会触发行锁，导致新事务阻塞，插入失败
        iPhantomReadService.insertIntoTestData(16L);

        System.out.println("插入数据后快照读：" + iPhantomReadService.selectList().size());
    }




}
