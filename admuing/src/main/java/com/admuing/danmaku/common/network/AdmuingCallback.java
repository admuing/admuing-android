package com.admuing.danmaku.common.network;

import com.admuing.danmaku.bean.DanmakuInfo;

/**
 * Created by duan .
 */
public class AdmuingCallback {

    public interface DanmakuCallback {
        void onSuccess(DanmakuInfo danmakuInfo);

        void onError(String msg);
    }
}
