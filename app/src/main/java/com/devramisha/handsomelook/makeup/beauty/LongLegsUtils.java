package com.devramisha.handsomelook.makeup.beauty;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Developer :- Ramish
 * time:2021-08
 * function: default function
 */
public class LongLegsUtils {

    /**
     * Big leg algorithm
     *
     * @param bitmap Original bitmap
     * @param rect   Stretch area
     * @param strength   Stretching strength [0.04f,0.10f]
     * @return After the picture
     */
    public static Bitmap longLeg(Bitmap bitmap, Rect rect, float strength) {
        //How many grids to divide the image
        int WIDTH = 200;
        int HEIGHT = 200;

        //Number of intersection coordinates
        int COUNT = (WIDTH + 1) * (HEIGHT + 1);

        //Used to save the coordinates of COUNT
        float[] verts = new float[COUNT * 2];


        float bmWidth = bitmap.getWidth();
        float bmHeight = bitmap.getHeight();

        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bmHeight * i / HEIGHT;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bmWidth * j / WIDTH;
                //X-axis coordinates are placed in even digits
                verts[index * 2] = fx;
                //The Y-axis coordinates are placed in odd places
                verts[index * 2 + 1] = fy;
                index += 1;
            }
        }

        int centerY = rect.centerY(),totalHeight = bitmap.getHeight();
        if(totalHeight < 5) return bitmap;
        warpLeg(COUNT,verts,centerY,totalHeight,rect.height(),strength);
        warpLeg(COUNT,verts,centerY,totalHeight,rect.height(),strength);

        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        return resultBitmap;
    }

    private static void warpLeg(int COUNT, float verts[], float centerY,int totalHeight,float region,float strength) {

        float  r = region / 2; //Zoom area strength

        for (int i = 0; i < COUNT * 2; i += 2) {
            //Calculate the distance between each coordinate point and the touch point
            float dy = verts[i + 1] - centerY;
            double e = (totalHeight - Math.abs(dy)) / totalHeight;
            if(Math.abs(dy) < r){
                //Elongation ratio
                double pullY = e * dy * strength;
                verts[i + 1] = (float) (verts[i + 1] + pullY);
            }else if(Math.abs(dy) < 2 * r || dy > 0){
                double pullY = e * e * dy * strength;
                verts[i + 1] = (float) (verts[i + 1] + pullY);
            }else if(Math.abs(dy) < 3 * r){
                double pullY = e * e * dy * strength /2;
                verts[i + 1] = (float) (verts[i + 1] + pullY);
            }else {
                double pullY = e * e * dy * strength /4;
                verts[i + 1] = (float) (verts[i + 1] + pullY);
            }

            //no problem
//            if(Math.abs(dy) < r){
//                //Elongation ratio
//                double pullY = e * dy * strength;
//                verts[i + 1] = (float) (verts[i + 1] + pullY);
//            }else if(Math.abs(dy) < 2 * r){
//                double pullY = e * e * dy * strength;
//                verts[i + 1] = (float) (verts[i + 1] + pullY);
//            }
        }
    }
}
