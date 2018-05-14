import com.fan.distributedlock.ConfigurationScan;
import com.fan.distributedlock.impl.LockRunningTest;
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

    private final Integer times = 10;

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

    @Test
    public void testRedisSpeed(){

    }

    @Test
    public void testMysqlSpeed(){

    }
}
