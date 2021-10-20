package com.devramisha.handsomelook.makeup.utils;

import android.util.Log;

import com.ding.makeup.BuildConfig;

/**
 * author: Md Ramish
 * time:2021-10
 * function: default function
 */
public class TimeAopUtils {

    private static long startTime;

    public static void start(){
        startTime = System.currentTimeMillis();
    }

    public static long end(String tag,String msgPre){
       long end = System.currentTimeMillis() - startTime;
       if(BuildConfig.DEBUG){
           Log.i("TimeAopUtils.java -> end method",msgPre+"-time consuming:"+ end);
       }
       return end;
    }
}
