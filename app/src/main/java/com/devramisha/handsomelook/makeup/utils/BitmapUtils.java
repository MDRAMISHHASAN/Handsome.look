package com.devramisha.handsomelook.makeup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;

/**
 * Developer: Md Ramish
 * time:2021-10
 * function: default function
 */
public class BitmapUtils {

    public static Bitmap getBitmapByAssetsName(Context context, String name){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inMutable = true;

        try {
            return BitmapFactory.decodeStream(context.getAssets().open(name),new Rect(),options);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.i("BitmapUtils.java,", "getBitmapByAssetsName: " + e);
        }

        return null;
    }
}
