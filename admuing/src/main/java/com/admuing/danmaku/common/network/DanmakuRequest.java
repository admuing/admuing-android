package com.admuing.danmaku.common.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.admuing.danmaku.common.util.AdvertisingIdClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


/**
 * Created by duan .
 */

public class DanmakuRequest {
    private static final String TAG = "BaseRequest";

    private DanmakuRequest() {
    }

    private static DanmakuRequest plainRequest;

    public static DanmakuRequest getPlainRequest() {
        if (plainRequest == null) {
            synchronized (DanmakuRequest.class) {
                if (plainRequest == null) {
                    plainRequest = new DanmakuRequest();
                }
            }
        }
        return plainRequest;
    }

    public void doGet(final Context context, final String url, final Map<String, Object> params, final NetCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (TextUtils.isEmpty(url)) {
                        callback.onError("empty url");
                        return;
                    }

                    String adId = "";
                    AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());
                    if (adInfo != null) {
                        adId = adInfo.getId();
                    }

                    params.put("idfa", adId);

                    String requestUrl = getUrlWithParas(url, params);
                    Log.d("GET", requestUrl);

                    URL url = new URL(requestUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if (200 == urlConnection.getResponseCode()) {

                        InputStream is = urlConnection.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while (-1 != (len = is.read(buffer))) {
                            baos.write(buffer, 0, len);
                            baos.flush();
                        }
                        String content = baos.toString("utf-8");
                        baos.close();
                        is.close();

                        if (!TextUtils.isEmpty(content)) {
                            callback.onSuccess(content);
                        } else {
                            callback.onError("empty body");
                        }
                    } else {
                        callback.onError("empty body");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                }

            }
        }).start();
    }

    private String getUrlWithParas(String url, Map<String, Object> params) {
        if (params.isEmpty()) {
            return url;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
            if (url == null) {
                return sb.substring(0, sb.length() - 1);
            }
            if (url.contains("?")) {
                return url + "&" + sb.substring(0, sb.length() - 1);
            } else {
                return url + "?" + sb.substring(0, sb.length() - 1);
            }
        }
    }
}
