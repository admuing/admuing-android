package com.admuing.danmaku.demo;

import android.app.Application;

import com.admuing.danmaku.Danmaku;


/**
 * Created by duan .
 */

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new Danmaku());
    }
}
