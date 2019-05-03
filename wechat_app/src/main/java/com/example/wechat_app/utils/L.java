package com.example.wechat_app.utils;

import android.util.Log;

public class L {
    private static final String TAG = "L";
    public static void d(String msg,Object... args){
        Log.d(TAG,String.format(msg,args));
    }
}
