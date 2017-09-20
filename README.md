# AdMuing

this is Android Version， IOS is [here](https://github.com/admuing/admuing-iOS)

[中文版说明](https://github.com/admuing/admuing-android/tree/master/CN)

## About

AdMuing is a video advertising Danmaku enhancement tool,Is an open source project created by years of research google & facebbok creative team，The purpose is to change the existing video advertising style is single，the problem of globalization is difficult to solve，With AdMuing, just a few lines of code can make the original video ads more vivid, interesting and interactive，Enhance the quality of developers APP ad, so that video ads more attractive to users.

## Demo

![image](https://github.com/admuing/admuing-android/blob/master/screenshots/1.jpg)

![image](https://github.com/admuing/admuing-android/blob/master/screenshots/2.png)

source code： **admuing**

Demo：**admuingdemo** include unity,vungle demo

## Features
-   Free：Completely free during use API，(Each key calls 10000 times per day because of server pressure)
-   Open Source：Each line of code on the GitHub is visible, fully transparent, and there is no need to worry about malicious code calls
-   Small: IOS and Android SDK are less than 50K
-   Simple: 4,5 lines of code, from the beginning to the finish, <5 minutes
-   Globalization: support 16 languages, 200+ countries, according to different regions display different languages, different Danmaku styles and colors
3rd Platform support: compatible support, Unity, Vungle and other mainstream Video video advertising，Does not affect or change any functionality or logic of the original Video SDK

## How to work

AdMuing SDK Danmaku SDK call API Danmaku display, according to user requests, IP, Token, advertising, ID, mobile phone language, such as intelligent matching Danmaku

1. according to the  video ads APP matching appropriate Danmaku, Danmaku from application market comments and web content crawl
2. According to the semantic recognition system, remove meaningless, negative comments on the Danmaku
3. According to the user's mobile phone language match Danmaku language ，currently supports 16 languages: simplified Chinese, traditional Chinese, English, Portuguese, French, Spanish, Russian, Arabic, German, Japanese, Korean, Hindi, Indonesian, Malay, Thai, Vietnamese, italian.
4. Show different Danmaku styles according to the user's country, such as the display of Danmaku effect in China, and the display effect of rolling commentary in the US area.
5. Danmaku color：Show different color of the Danmaku according to the color and taboo color of the users in different countries，example：red, yellow and blue in china，green and white in Saudi Arabia，Orange, white,and green in India


## How to use

### Register [address](http://register.admuing.com/?recom=github) to get 

### dependencies 
    
    compile 'com.github.admuing:admuing:1.0'

### permission

    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    
### add your appKey in 'application'
 
    <application>
    ...
    <meta-data android:name="appKey" android:value="your appKey" />
    ...
    </application>

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

## Remarks

- Current compatible ad platform：Unity,Vungle,AdTiming，Constantly update....
- Theory can support all video Ad SDK，But need to test， If need to support more platforms, please contact us
support@admuing.com
-   Android Studio 1.5 or higher
- 	Target Android API level 23 or higher
- 	MinSdkVersion level 19 or higher

## Support

1. create an issue on the github issue page
2. support email: support@admuing.com

We will reply as soon as possible
