package com.bing.library.utils;

import java.io.File;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2019/2/20.
 */

public class ReflectUtils {
    /**
     * 通过反射获取某对象，并设置私有可访问
     * @param obj
     * @param clazz
     * @param filed
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFiled(Object obj,Class<?> clazz,String filed) throws NoSuchFieldException, IllegalAccessException {
        Field localField = clazz.getDeclaredField(filed);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    /**
     * 给某属性赋值，并设置私有可访问
     * @param obj 该属性所属类的对象
     * @param clazz 该属性所属类
     * @param value 值
     */
    public static void setFiled(Object obj, Class<?> clazz, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field localField = clazz.getDeclaredField("dexElements");
        localField.setAccessible(true);
        localField.set(obj,value);
    }

    /**
     * 通过反射获取BaseDexclassLoader对象中的pathList类
     * @param baseDexclassLoader  BaseDexclassLoader对象
     * @return PathList对象
     */
    public static Object getPathList(Object baseDexclassLoader) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        return getFiled(baseDexclassLoader,Class.forName("dalvik.system.BaseDexClassLoader"),"pathList");
    }

    /**
     * 通过反射获取BaseDexclassLoader对象中的pathList对象，再获取dexElements对象
     * @param paramObject pathList对象
     * @return dexElements对象
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getDexElements(Object paramObject) throws NoSuchFieldException, IllegalAccessException {
        return getFiled(paramObject,paramObject.getClass(),"dexElements");
    }

}
