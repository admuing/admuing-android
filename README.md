# admuing-android

dev by [ADTIMING](http://www.adtiming.com/)

## Intro

source code： **danmaku**

Demo：**danmakudemo**

-   Android Studio 1.5 or higher
- 	Target Android API level 23 or higher
- 	MinSdkVersion level 19 or higher
- 	Recommended: create an Adtiming account and register an app.

## Config

### permissions 

    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    
### add your appId in 'application'
    <application>
    ...
    <meta-data android:name="appId" android:value="your appId" />
    ...
    </application>

## How to Use


### register in your Application

    public class DemoApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            registerActivityLifecycleCallbacks(new Danmaku());
        }
    }

### show

    Danmaku.show("packageName");
    
### hide

    Danmaku.hide();
