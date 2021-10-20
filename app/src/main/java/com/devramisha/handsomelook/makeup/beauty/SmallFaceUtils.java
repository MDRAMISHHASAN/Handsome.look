package com.devramisha.handsomelook.makeup.beauty;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.List;

/**
 * @author by Ramish
 *         function:Face-lifting function
 */

public class SmallFaceUtils {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;

    /**
     * Face-lifting algorithm
     *
     * @param bitmap      Original bitmap
     * @return After the picture
     */
    public static Bitmap smallFaceMesh(Bitmap bitmap, List<Point> leftFacePoint,List<Point> rightFacePoint,Point centerPoint, int level) {

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
        int r = 180 + 15 * level;
        warp(COUNT,verts,leftFacePoint.get(16).x,leftFacePoint.get(16).y,centerPoint.x,centerPoint.y,r);
        warp(COUNT,verts,leftFacePoint.get(46).x,leftFacePoint.get(46).y,centerPoint.x,centerPoint.y,r);

        warp(COUNT,verts,rightFacePoint.get(16).x,rightFacePoint.get(16).y,centerPoint.x,centerPoint.y,r);
        warp(COUNT,verts,rightFacePoint.get(46).x,rightFacePoint.get(46).y,centerPoint.x,centerPoint.y,r);

        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
//        canvas.drawBitmap(bitmap,0,0,paint);
//        paint.setColor(Color.RED);
//        canvas.drawCircle(leftFacePoint.get(16).x,leftFacePoint.get(16).y,3,paint);
//        canvas.drawCircle(leftFacePoint.get(46).x,leftFacePoint.get(46).y,3,paint);
//        canvas.drawCircle(rightFacePoint.get(16).x,rightFacePoint.get(16).y,3,paint);
//        canvas.drawCircle(rightFacePoint.get(46).x,rightFacePoint.get(46).y,3,paint);
//        canvas.drawCircle(centerPoint.x,centerPoint.y,3,paint);
        canvas.drawBitmapMesh(bitmap,WIDTH, HEIGHT,verts,0,null,0,null);
        return resultBitmap;
    }

    private static void warp(int COUNT,float verts[],float startX, float startY, float endX, float endY,int r) {
        //level [0,4]

        //int r = 200; default 200

        //Calculate drag distance
        float ddPull = (endX - startX) * (endX - startX) + (endY - startY) * (endY - startY);
        float dPull = (float) Math.sqrt(ddPull);
        //dPull = screenWidth - dPull >= 0.0001f ? screenWidth - dPull : 0.0001f;
        if(dPull < 2 * r){
            dPull = 2 * r;
        }

        int powR = r * r;
        int index = 0;
        int offset = 1;
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
                //Border area is not processed
                if(i < offset || i > HEIGHT - offset || j < offset || j > WIDTH - offset){
                    index = index + 1;
                    continue;
                }
                //Calculate the distance between each coordinate point and the touch point
                float dx = verts[index * 2] - startX;
                float dy = verts[index * 2 + 1] - startY;
                float dd = dx * dx + dy * dy;

                if (dd < powR) {
                    //Deformation coefficient
                    double e = (powR - dd) * (powR - dd) / ((powR - dd + dPull * dPull) * (powR - dd + dPull * dPull));
                    double pullX = e * (endX - startX);
                    double pullY = e * (endY - startY);
                    verts[index * 2] = (float) (verts[index * 2] + pullX);
                    verts[index * 2 + 1] = (float) (verts[index * 2 + 1] + pullY);
                }
                index = index + 1;
            }
        }
    }

}
