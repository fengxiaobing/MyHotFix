package com.bing.myhotfix;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bing.library.FixDexUtils;

/**
 * Created by Administrator on 2019/2/20.
 */

public class BaseApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        //加载热修复Dex文件
        FixDexUtils.loadFixDex(base);
    }
}
