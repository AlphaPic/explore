package com.fan.autowiki.task;

import com.fan.autowiki.bean.RequestMappingVO;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author:fanwenlong
 * @date:2018-06-11 16:05:43
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail: 扫描任务，目的是如果已经把代码拉取下来并且通过编译之后，那么将代码重新拉取到数据库中去
 */
public class ScanTask implements Runnable{


    @Override
    public void run() {

    }

    /**
     * 扫描Bean
     */
    public void scanForBean(){

    }

    /**
     * 扫描RequestMapping
     */
    public void scanForRequestMapping(String path){
        System.out.println(path);
        File file = new File(path);
        if(file.isDirectory()){
            String[] subFiles = file.list();
            for (String subFile : subFiles){
                scanForRequestMapping(path + "\\" + subFile);
            }
        }else{
            String filePath = file.getPath();
            if (filePath.endsWith(".class")){
                generateRequestObjFromClassPath(filePath);
            }
        }
    }

    /**
     * 生成RequestMapping的存储对象
     * @param filePath
     */
    private RequestMappingVO generateRequestObjFromClassPath(String filePath) {
        if(filePath == null || filePath.isEmpty()){
            return null;
        }
        Class clazz = ClassUtils.getClassFromFileSystem(filePath);
        if(clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            if (methods != null){
                for (Method method : methods){
                    Annotation rpa = method.getAnnotation(RequestMapping.class);
                    if (rpa == null)
                        continue;

                    Class[] paramTypes = method.getParameterTypes();
                    if (paramTypes == null || paramTypes.length != 1){
                        continue;
                    }
                    Class returnType = method.getReturnType();
                }
            }
        }
        return null;
    }


    public static void main(String[] args){
        new ScanTask().scanForRequestMapping("E:\\data\\lab\\WIKITMP\\csa\\csa-user-sso\\target\\classes\\com");
    }
}
