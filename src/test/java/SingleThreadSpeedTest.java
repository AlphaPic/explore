import com.fan.distributedlock.ConfigurationScan;
import com.fan.distributedlock.config.ZookeeperConfig;
import com.fan.distributedlock.impl.LockRunningTest;
import com.fan.distributedlock.impl.ZookeeperDistributedLockServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author:fanwenlong
 * @date:2018-05-14 18:07:58
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigurationScan.class)
public class SingleThreadSpeedTest {
    @Autowired
    private LockRunningTest test;
    @Autowired
    private ZookeeperDistributedLockServiceImpl lock;
    @Autowired
    private ZookeeperConfig config;

    private final Integer times = 10000;

    @Test
    public void testZookeeperSpeed(){
        long start = System.currentTimeMillis();
        Integer count = times;
        while (count-- > 0) {
            test.lockWithZookeeper();
        }
        long end = System.currentTimeMillis();
        System.out.println("获取" + times + "次锁一共花了" + (end - start) + "ms");
    }

//    @Test
//    public void testRedisSpeed(){
//
//    }
//
//    @Test
//    public void testMysqlSpeed(){
//
//    }

    //测试zk的写性能
//    @Test
    public void testZookeeperWrite(){
        if(lock.initZookeeper("How can I stop loving you:)") == false){
            return;
        }
        int count   = times;
        int writen  = 0;
        boolean flag = false;
        long start = System.currentTimeMillis();
        while (count-- > 0){
            flag = lock.unsafeWriteData(config.Node,"How can I stop loving you:)");
            writen = (flag ? (writen + 1) : writen);
        }
        long end = System.currentTimeMillis();
        System.out.println("写入(" + writen + "/" + times + ")次数据一共花了" + (end - start) + "ms");
    }

    //测试zk的读性能
//    @Test
    public void testZookeeperRead(){
        if(lock.initZookeeper("How can I stop loving you:)") == false){
            return;
        }
        int count = times;
        int writen  = 0;
        boolean flag = false;
        long start = System.currentTimeMillis();
        while (count-- > 0){
            flag = lock.unsafeReadData(config.Node);
            writen = (flag ? (writen + 1) : writen);
        }
        long end = System.currentTimeMillis();
        System.out.println("读出(" + writen + "/" + times + ")次数据一共花了" + (end - start) + "ms");
    }
}
