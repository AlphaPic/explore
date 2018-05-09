package com.fan.distributedlock.consts;

/**
 * @author:fanwenlong
 * @date:2018-05-08 14:40:23
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public enum METHOD_NAME_PERMISSION {
    LOCK1("lock1"),
    LOCK2("lock2"),
    LOCK3("lock3"),
    LILY("lily");

    private String name;

    METHOD_NAME_PERMISSION(String name) {
        this.name = name;
    }

    public static boolean isNameBeenAllowed(String name){
        if(name == null || name.isEmpty()){
            return false;
        }
        METHOD_NAME_PERMISSION[] permissions = METHOD_NAME_PERMISSION.values();
        for (METHOD_NAME_PERMISSION method_name : permissions){
            if(name.equals(method_name.name)){
                return true;
            }
        }
        return false;
    }
}
