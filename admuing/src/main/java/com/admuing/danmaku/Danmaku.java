package com.admuing.danmaku;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;


/**
 * Created by duan .
 */

public class Danmaku implements Application.ActivityLifecycleCallbacks {
    private static WeakReference<Activity> activityRef;

    public static void show(String packageName) {
        DanmakuImp danmakuImp = DanmakuImp.getInstance().get();
        if (danmakuImp != null && activityRef != null && activityRef.get() != null) {
            danmakuImp.show(activityRef.get(), packageName);
        }
    }

    public static void hide() {
        DanmakuImp danmakuImp = DanmakuImp.getInstance().get();
        if (danmakuImp != null) {
            danmakuImp.hide();
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        activityRef = new WeakReference<>(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
