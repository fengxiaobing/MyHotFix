package com.bing.myhotfix;

import android.content.Context;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bing.library.FixDexUtils;
import com.bing.library.utils.Constants;
import com.bing.library.utils.FileUtils;
import com.bing.myhotfix.utils.ParamsSort;

import java.io.File;
import java.io.IOException;

public class SecondActivity extends BaseActivity {

    String name = "bing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void show(View view) {
        ParamsSort sort = new ParamsSort();
        sort.math(this);
    }

    public void fix(View view) {
        fixBug();
    }

    //   /storage/emulated/0/classes.dex
    private void fixBug() {
        //通过服务器接口下载dex文件，v1.3.3版本有某一个热修复dex包

        File sourceFile = new File(Environment.getExternalStorageDirectory(), Constants.DEX_NAME);

        //目标路径  私有目录里的临时文件夹 odex
        File targetFile = new File(getDir(Constants.DEX_DIR, Context.MODE_PRIVATE).getAbsolutePath()
                + File.separator + Constants.DEX_NAME);

        //如果存在，之前修复过classes2.dex  清理
        if(targetFile.exists()){
            targetFile.delete();
            Toast.makeText(this, "删除已经存在的dex文件", Toast.LENGTH_SHORT).show();
        }

        //复制修复包dex文件到App私有目录
        try {
            FileUtils.copyFile(sourceFile,targetFile);
            Toast.makeText(this, "复制dex文件完成", Toast.LENGTH_SHORT).show();
            //开始修复
            FixDexUtils.loadFixDex(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
