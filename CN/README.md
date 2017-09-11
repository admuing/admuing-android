# AdMuing

这里是安卓版本， IOS版本[在此](https://github.com/admuing/admuing-iOS)

## 关于

AdMuing是一个视频广告弹幕增强工具，是由多年研究Google,Facebook的创意团队创建的开源项目，目的是为了改变现有视频广告样式单一，很难全球化的问题，通过AdMuing简单几行代码就可以让原有的视频广告更生动，更有趣，更有互动性，提升开发者APP广告质量，让视频广告对用户更具有吸引力。

## 示例

![image](https://github.com/admuing/admuing-android/blob/master/screenshots/1.jpg)

![image](https://github.com/admuing/admuing-android/blob/master/screenshots/2.png)

源码： **admuing**

示例项目：**admuingdemo** 包括unity,vungle示例

## 特色

1. 免费：在使用期间，完全免费 (因为服务器压力每天限制10,000次广告请求)
2. 开源：GitHub上每行代码清晰可见，完全透明化，完全不用担心恶意代码程序调用
3. 小巧：IOS和Android SDK 仅仅不到50K
4. 简单：4、5行代码，从开始到测试完成 <5分钟
5. 全球化：支持16种语言，200+国家，根据不同地区的APP显示不同语言文字，不同弹幕样式及颜色，
6. 三方平台支持：兼容支持Unity,Vungle等主流Video视频广告，集成后立刻显示，不影响也不改变原有的Video SDK任何功能及逻辑

## 如何使用

AdMuing SDK弹幕SDK调用API进行弹幕展示，根据用户请求IP，AppKey,广告ID，手机语言等进行弹幕智能匹配

1. 根据所播放的视频广告APP进行匹配适合的弹幕，弹幕来源于应用市场评论及网页内容爬取
2. 根据语意识别系统，去除无意义，负面的评论弹幕
3. 根据用户手机语言选择弹幕语言，目前支持16种语言：简体中文，繁体中文，英语，葡萄牙语，法语，西班牙语，俄罗斯语，阿拉伯语，德语，日语，印地语，韩语，印尼语，马来语，泰语，越南语，意大利语。
4. 根据用户所在国家，展示不同弹幕样式，例如：中国展示弹幕效果，美国地区展示评论滚动效果
5. 弹幕颜色：根据不同国家的用户所喜欢的颜色和禁忌的颜色，展示不同弹幕颜色，例如，中国展示红，黄，蓝，沙特展示绿色，白色，印度展示橙色，白色，绿色

### 注册[地址](http://register.admuing.com/)来获得AppKey

### 项目依赖
    
    compile 'com.github.admuing:admuing:1.0'

### 权限

    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    
### 添加appKey至'application'标签内
 
    <application>
    ...
    <meta-data android:name="appKey" android:value="your appKey" />
    ...
    </application>

### 在Application中注册回调

    public class DemoApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            registerActivityLifecycleCallbacks(new Danmaku());
        }
    }

### 显示

    Danmaku.show("packageName");
    
### 隐藏

    Danmaku.hide();

## 备注

- 当前支持平台：Unity,Vungle,AdTiming，不断更新中...
- 理论支持全部视频广告SDK，但需要进行调试，如果需要支持更多平台，请联系support@admuing.com
-   Android Studio 1.5 或者更高版本
- 	Target Android API level 23 或者更高版本
- 	MinSdkVersion level 19 或者更高版本

## 支持

1. 在Github创建issue
2. 支持邮箱：support@admuing.com

我们会尽力回复
