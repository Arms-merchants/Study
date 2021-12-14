package com.arms.flowview.utils;

import android.app.Application;
import android.content.Context;

/**
 * author : heyueyang
 * time   : 2021/12/07
 * desc   :
 * version: 1.0
 */
public class Utils {

    private static Application mApp;


    public static void init(Application application) {
        mApp = application;
    }

    public static Context getApp() {
        return mApp;
    }

}
