import com.fan.distributedlock.ConfigurationScan;
import com.fan.distributedlock.impl.DistributedLockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * @author:fanwenlong
 * @date:2018-05-07 17:03:10
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigurationScan.class)
public class DistributedLockTestMysql {
    @Autowired
    @Qualifier("mysql")
    private DistributedLockService service;

    @Test
    public void TestLock(){
//        assertNotNull(service);
//
//        assertEquals(service.lock(),true);
//        System.out.println("Got it!!!");
    }

    @Test
    public void TestRelease(){
//        assertNotNull(service);
//
//        assertEquals(service.release(),true);
//
//        System.out.println("Lose it!!!");
    }

    @Test
    public void TestLockBySelect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertNotNull(service);

        Method method = service.getClass().getMethod("lockWithSelectUpdate");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
            }
        });

        assertEquals(method.invoke(service),true);

        System.out.println("Select lock got successful!!!");
    }
}
