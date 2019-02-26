package com.xq.mychangeskin;

import android.app.Application;

public class MyApp extends Application {
    public static int theme = 0;

    public static MyApp application;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
    }

    public static MyApp getApplication() {
        return application;
    }

}
