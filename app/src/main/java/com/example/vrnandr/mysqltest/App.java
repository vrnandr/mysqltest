package com.example.vrnandr.mysqltest;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context mcontext;

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext = getApplicationContext();
    }

    public static Context getContext() {
        return mcontext;
    }
}
