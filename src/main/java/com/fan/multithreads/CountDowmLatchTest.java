package com.fan.multithreads;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author:fanwenlong
 * @date:2018-04-25 16:57:08
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class CountDowmLatchTest {
    static Random rand = new Random(45);

    static class OneInstanceService {
        public int i_am_has_state;

        private static OneInstanceService test;

        private OneInstanceService() {
            i_am_has_state = rand.nextInt(200) + 1;
        }

        public static OneInstanceService getTest1() {
            if (test == null) {
                synchronized (OneInstanceService.class) {
                    if (test == null) {
                        test = new OneInstanceService();
                    }
                }
            }
            return test;
        }

        public static void reset() {
            test = null;
        }

    }
    public static void main(String[] args) throws InterruptedException {
        for (;;) {
            System.out.println("---------------start---------------");
            final CountDownLatch latch  = new CountDownLatch(1);
            final CountDownLatch end    = new CountDownLatch(100);
            for (int i = 0; i < 100; i++) {
                Thread t1 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            latch.await();
                            OneInstanceService one = OneInstanceService.getTest1();
                            if (one.i_am_has_state == 0) {
                                System.out.println("one.i_am_has_state == 0 进程结束");
                                System.exit(0);
                            }
                            System.out.println(one.i_am_has_state);
                            end.countDown();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t1.start();
            }
            latch.countDown();
            end.await();
            System.out.println("---------------end---------------");
            Thread.sleep(1000);
            OneInstanceService.reset();
        }
    }
}
