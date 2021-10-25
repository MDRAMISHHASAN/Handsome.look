package com.devramisha.handsomelook.makeup.utils;

import android.util.Log;

import com.devramisha.handsomelook.BuildConfig;

/**
 * author: Md Ramish
 * time:2021-10
 * function: default function
 */
public class TimeAopUtils {

    private static long startTime;

    public static void start(){
        startTime = System.currentTimeMillis();
        Log.i("TimeAopUtils.java,start","Start Time" + startTime);
    }

    public static long end(String tag,String msgPre){
       long end = System.currentTimeMillis() - startTime;
       if(BuildConfig.DEBUG){
           Log.i("TimeAopUtils.java.end",msgPre+"-time consuming:"+ end);
       }
       return end;
    }
}
