package com.bing.library;

import android.content.Context;

import com.bing.library.utils.ArrayUtils;
import com.bing.library.utils.Constants;
import com.bing.library.utils.FileUtils;
import com.bing.library.utils.ReflectUtils;

import java.io.File;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by Administrator on 2019/2/20.
 */

public class FixDexUtils {

    //存放需要修复的dex集合
    private static HashSet<File> loadedDex = new HashSet<>();

    static {
        //修复前  进行清理工作
        loadedDex.clear();
    }

    /**
     * 加载热修复的dex文件
     * @param context
     */
    public static void loadFixDex(Context context) {
        if(context == null){
          return;
        }
        //Dex 文件目录（私有目录中，存在已经复制郭磊的热修复包）
        File fileDir = context.getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);
        File[] listFiles = fileDir.listFiles();
        //遍历私有目录中所有的文件
        for (File file : listFiles) {
            //找到修复包  加入到集合
            if(file.getName().endsWith(Constants.DEX_SUFFIX) && !"classes.dex".equals(file.getName())) {
                loadedDex.add(file);
            }
        }

        //模拟类加载器
        createDexClassLoader(context,fileDir);

    }

    /**
     * 创建加载补丁的DexClassLoader
     * @param context 上下文
     * @param fileDir dex文件目录
     */
    private static void createDexClassLoader(Context context, File fileDir) {
        //创建一个临时的解压的目录（先解压到该目录，再去加载java）
       String optmizedDir = fileDir.getAbsolutePath() + File.separator + "opt.dex";
       //不存在就创建
        File fopt = new File(optmizedDir);
        if(!fopt.exists()){
            //创建多级目录
            fopt.mkdir();
        }
        for (File dex : loadedDex) {
            //每遍历一个要修复的dex文件 就需要插装一次
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(),optmizedDir,
                    null,context.getClassLoader());
            hotfix(classLoader,context);
        }
    }

    /**
     * 热修复
     * @param classLoader 自有的类加载器，加载了修复包的DexClassLoader
     * @param context 上下文
     */
    private static void hotfix(DexClassLoader classLoader, Context context) {
        //获取系统的PathClassLoader
        PathClassLoader pathLoader = (PathClassLoader) context.getClassLoader();


        try {
            //获取自有的dexElements数组对象
            Object  myDexElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(classLoader));
            //获取系的dexElements数组对象
            Object systemDexElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(pathLoader));
            //合并成新的dexElements数组对象
            Object dexElements =  ArrayUtils.combineArray(myDexElements,systemDexElements);
            //通过反射再去获取系统的pathList对象
            Object systemPathList = ReflectUtils.getPathList(pathLoader);
            //重新赋值给系统的pathList属性  --- 修改了pathList中的dexElements数组对象
            ReflectUtils.setFiled(systemPathList,systemPathList.getClass(),dexElements);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
