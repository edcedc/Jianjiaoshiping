package com.yc.jianjiao.mar;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.nanchen.crashmanager.CrashApplication;
import com.yc.jianjiao.service.InitializeService;

public class MyApplication extends CrashApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        ViewTarget.setTagId(R.id.tag_glide);//项目glide 图片ID找不到  会报null

        InitializeService.start(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

}
