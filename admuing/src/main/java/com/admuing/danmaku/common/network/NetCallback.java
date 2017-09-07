package com.admuing.danmaku.common.network;

/**
 * Created by duan .
 */

public interface NetCallback {
    void onSuccess(String body);

    void onError(String error);
}
