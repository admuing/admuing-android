package com.admuing.danmaku.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.admuing.danmaku.Danmaku;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.mediation.IUnityAdsExtendedListener;
import com.vungle.publisher.AdConfig;
import com.vungle.publisher.VungleAdEventListener;
import com.vungle.publisher.VungleInitListener;
import com.vungle.publisher.VunglePub;

public class MainActivity extends Activity {
    private boolean showDanmaku = true;
    private VunglePub vunglePub = VunglePub.getInstance();
    private AdConfig globalAdConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch aSwitch = (Switch) findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showDanmaku = isChecked;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }

    @Override
    public void onDestroy() {
        vunglePub.clearEventListeners();
        super.onDestroy();
    }

    public void vungle(View view) {
        final Button btn = (Button) view;
        final String placementId = "TESTPLA56079";
        if (vunglePub.isAdPlayable(placementId)) {
            vunglePub.playAd(placementId, globalAdConfig);
            return;
        }
        if (!vunglePub.isInitialized()) {
            vunglePub.init(this, "59881040a451bbc870001236", new String[]{placementId}, new VungleInitListener() {
                @Override
                public void onSuccess() {
                    globalAdConfig = vunglePub.getGlobalAdConfig();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn.setText("vungle init success and start loading");
                            vungleLoad(btn, placementId);
                        }
                    });
                }

                @Override
                public void onFailure(Throwable e) {

                }
            });
            btn.setText("vungle init");
        } else {
            vungleLoad(btn, placementId);
            btn.setText("vungle loading");
        }


    }

    private void vungleLoad(final Button button, String placementId) {
        vunglePub.clearAndSetEventListeners(new VungleAdEventListener() {
            @Override
            public void onAdEnd(String placementReferenceId, boolean wasSuccessfulView, boolean wasCallToActionClicked) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Danmaku.hide();
                    }
                });
            }

            @Override
            public void onAdStart(String placementReferenceId) {
                if (!showDanmaku) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Danmaku.show("");
                    }
                });

            }

            @Override
            public void onUnableToPlayAd(String placementReferenceId, String reason) {
            }

            @Override
            public void onAdAvailabilityUpdate(String placementReferenceId, boolean isAdAvailable) {
                if (isAdAvailable) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setText("vungle ready");
                        }
                    });
                }
            }
        });

        vunglePub.loadAd(placementId);
    }

    public void unity(View view) {
        final Button btn = (Button) view;
        if (UnityAds.isReady()) {
            UnityAds.show(this);
            return;
        }
        UnityAds.initialize(this, "1528027", new IUnityAdsExtendedListener() {

            @Override
            public void onUnityAdsClick(String placementId) {
            }

            @Override
            public void onUnityAdsPlacementStateChanged(String placementId, UnityAds.PlacementState oldState, UnityAds.PlacementState newState) {

            }

            @Override
            public void onUnityAdsReady(String placementId) {
                btn.setText("unity ready");
            }

            @Override
            public void onUnityAdsStart(String placementId) {
                if (showDanmaku) {
                    Danmaku.show("");
                }
            }

            @Override
            public void onUnityAdsFinish(String placementId, UnityAds.FinishState result) {
                Danmaku.hide();
            }

            @Override
            public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
            }
        });

        btn.setText("unity loading");
    }

}
