package com.fan.utils;

/**
 * @author:fanwenlong
 * @date:2018-03-14 15:10:28
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class FinalizeMethod {

    private static FinalizeMethod hook = null;

    public static void main(String[] args) throws InterruptedException {
        hook = new FinalizeMethod();
        hook = null;
        System.gc();
        Thread.sleep(100);
        if(hook == null){
            System.out.println("已死");
        }else {
            System.out.println("仍活");
        }

        hook = null;
        System.gc();
        Thread.sleep(500);
        if(hook == null){
            System.out.println("已死");
        }else {
            System.out.println("仍活");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("执行回收程序");
        hook = this;
    }
}
