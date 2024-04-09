package com.study;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.study.jetpack.mvi.net.RetrofitUtil;
import com.study.mvi.utils.ObjectStore;

public class MyApplication extends Application {

    private static MyApplication application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ObjectStore.INSTANCE.setContext(this);
        //初始化Retrofit
        RetrofitUtil.INSTANCE.initRetrofit();
        //基于Lifecycle，监听Application的生命周期
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new MyApplicationLifecycleObserver());
        //SP优化
//        SPHook.INSTANCE.optimizeSpTask();
    }

    public static MyApplication getApplication() {
        return application;
    }
}
