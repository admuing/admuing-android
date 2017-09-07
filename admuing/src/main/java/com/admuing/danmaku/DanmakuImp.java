package com.admuing.danmaku;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.WindowManager;

import com.admuing.danmaku.bean.DanmakuInfo;
import com.admuing.danmaku.common.network.AdmuingCallback;
import com.admuing.danmaku.common.network.LoadService;
import com.admuing.danmaku.view.DanmakuCallback;
import com.admuing.danmaku.view.DanmakuTextureView;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

/**
 * Created by duan .
 */

class DanmakuImp implements DanmakuCallback {
    private final String TAG = "DanmakuImp";
    private DanmakuTextureView danmakuTextureView;
    private WindowManager windowManager;
    private AtomicBoolean hasShow = new AtomicBoolean(false);
    private static WeakReference<DanmakuImp> mInstances = null;

    private DanmakuImp() {

    }

    public static synchronized WeakReference<DanmakuImp> getInstance() {
        if (mInstances == null || mInstances.get() == null) {
            mInstances = new WeakReference<>(new DanmakuImp());
        }
        return mInstances;
    }


    public void show(final Activity activity, final String packageName) {
        if (hasShow.get()) {
            return;
        }

        hasShow.set(true);
        danmakuTextureView = new DanmakuTextureView(activity);
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCHABLE | FLAG_FULLSCREEN;
        params.format = PixelFormat.TRANSLUCENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        windowManager.addView(danmakuTextureView, params);
        danmakuTextureView.setFitsSystemWindows(true);
        AdmuingCallback.DanmakuCallback callback = new AdmuingCallback.DanmakuCallback() {
            @Override
            public void onSuccess(final DanmakuInfo danmakuInfo) {
                if (danmakuTextureView != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            danmakuTextureView.setType(danmakuInfo.getType());
                            danmakuTextureView.setFontColors(danmakuInfo.getFontColors());
                            danmakuTextureView.setData(danmakuInfo.getDanmakus());
                            danmakuTextureView.setCallback(DanmakuImp.this);
//                            View decorView = activity.getWindow().getDecorView();
//                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//                            decorView.setSystemUiVisibility(uiOptions);
                        }
                    });
                }
            }

            @Override
            public void onError(String msg) {

            }
        };

        LoadService.getInstance().loadDanmakuList(activity.getApplicationContext(), packageName, callback);
    }

    public void hide() {
        hasShow.set(false);
        try {
            if (danmakuTextureView != null && windowManager != null) {
                windowManager.removeViewImmediate(danmakuTextureView);
                danmakuTextureView = null;
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void noMore(Context context) {
        hide();
    }


}
