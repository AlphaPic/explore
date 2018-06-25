package com.fan.autowiki.task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author:fanwenlong
 * @date:2018-06-11 19:00:05
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ClassUtils {
    private static ClassLoader defaultClassLoader;

    static {
        defaultClassLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String path) throws ClassNotFoundException {
                int packageIndex = path.indexOf("com");
                if(packageIndex < 0){
                    return null;
                }
                String packageName = path.substring(packageIndex);
                packageName = packageName.replace("\\",".");

                Class clazz = null;
                try {
                    byte[] classBytes = Files.readAllBytes(Paths.get(path));
                    if(classBytes == null || classBytes.length < 0){
                        return null;
                    }
                    clazz = defineClass(packageName,classBytes,0,classBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    return clazz;
                }
            }
        };
    }

    /**
     * 从文件系统中获取类对象
     * @param path
     * @return
     */
    public static Class getClassFromFileSystem(String path){
        try {
            return defaultClassLoader.loadClass(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
