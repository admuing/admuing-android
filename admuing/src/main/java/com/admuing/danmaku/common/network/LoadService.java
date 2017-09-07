package com.admuing.danmaku.common.network;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.admuing.danmaku.bean.DanmakuInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;


public class LoadService {
    private static final String TAG = "LoadService";
    private static final String danmakuList = "http://api.admuing.com/danmakuList";

    private static class HolderClass {
        static final LoadService instance = new LoadService();
    }

    public static LoadService getInstance() {
        return HolderClass.instance;
    }

    public void loadDanmakuList(Context context, String package_name, final AdmuingCallback.DanmakuCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("package_name", package_name);
        params.put("app_package_name", context.getPackageName());
        params.put("timestamp", System.currentTimeMillis() / 1000);
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo == null || appInfo.metaData == null) {
                Log.d("LoadService", "empty appKey");
                return;
            }
            String appKey = appInfo.metaData.getString("appKey");
            if (TextUtils.isEmpty(appKey)) {
                Log.d("LoadService", "empty appKey");
                return;
            }
            params.put("app_key", appKey);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Locale loc = Locale.getDefault();
        String language = loc.getLanguage().toLowerCase(Locale.US);
        if (language.length() == 0) {
            language = "en";
        }
        params.put("language", language);

        NetCallback callBack = new NetCallback() {
            @Override
            public void onSuccess(String body) {
                try {
                    final JSONObject respObj = new JSONObject(body);
                    int status = respObj.optInt("status", 0);
                    if (status == 0) {
                        String msg = respObj.optString("msg");
                        Log.e(TAG, "load ad fail:" + msg);
                        callback.onError(msg);
                    } else {
                        DanmakuInfo danmakuInfo = handleDanmakuList(respObj);
                        if (danmakuInfo != null) {
                            callback.onSuccess(danmakuInfo);
                        } else {
                            callback.onError("empty danmakuInfo");
                        }
                        Log.d(TAG, "load success");
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "load fail", e);
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "加载广告失败:" + error);
                callback.onError(error);
            }
        };

        DanmakuRequest.getPlainRequest().doGet(context, danmakuList, params, callBack);
    }


    private DanmakuInfo handleDanmakuList(JSONObject jsonObject) {
        DanmakuInfo danmakuInfo = new DanmakuInfo();
        LinkedList<String> list = new LinkedList<>();
        JSONArray array = jsonObject.optJSONArray("danmakus");
        int len = array.length();
        for (int a = 0; a < len; a++) {
            String d = array.optString(a);
            list.add(d);
        }
        danmakuInfo.setDanmakus(list);
        danmakuInfo.setType(jsonObject.optInt("type"));
        JSONArray colors = jsonObject.optJSONArray("colors");
        int[] fontColors = new int[colors.length()];
        for (int a = 0; a < colors.length(); a++) {
            fontColors[a] = Color.parseColor(colors.optString(a));
        }
        danmakuInfo.setFontColors(fontColors);
        Log.d(TAG, danmakuInfo.toString());
        return danmakuInfo;
    }
}
