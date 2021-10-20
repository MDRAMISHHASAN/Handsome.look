package com.devramisha.handsomelook.makeup.beauty;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.ding.makeup.utils.TimeAopUtils;

/**
 * @author by Ramish
 *         time 2021/9
 *         function: Enlarged eyes
 */

public class MagnifyEyeUtils {
    /**
     *  Eye magnification algorithm
     * @param bitmap      Original bitmap
     * @param centerPoint Zoom in the center point
     * @param radius      Magnification radius
     * @param sizeLevel    Amplify  [0,4]
     * @return The picture after magnifying the eyes
     */
    public static Bitmap magnifyEye(Bitmap bitmap, Point centerPoint, int radius, float sizeLevel) {
        TimeAopUtils.start();
        Bitmap dstBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
        int left = centerPoint.x - radius < 0 ? 0 : centerPoint.x - radius;
        int top = centerPoint.y - radius < 0 ? 0 : centerPoint.y - radius;
        int right = centerPoint.x + radius > bitmap.getWidth() ? bitmap.getWidth() - 1 : centerPoint.x + radius;
        int bottom = centerPoint.y + radius > bitmap.getHeight() ? bitmap.getHeight() - 1 : centerPoint.y + radius;
        int powRadius = radius * radius;

        int offsetX, offsetY, powDistance, powOffsetX, powOffsetY;

        int disX, disY;

        //When it is a negative number, it is reduced
        float strength = (5 + sizeLevel * 2) / 10;

        for (int i = top; i <= bottom; i++) {
            offsetY = i - centerPoint.y;
            for (int j = left; j <= right; j++) {
                offsetX = j - centerPoint.x;
                powOffsetX = offsetX * offsetX;
                powOffsetY = offsetY * offsetY;
                powDistance = powOffsetX + powOffsetY;

                if (powDistance <= powRadius) {
                    double distance = Math.sqrt(powDistance);
                    double sinA = offsetX / distance;
                    double cosA = offsetY / distance;

                    double scaleFactor = distance / radius - 1;
                    scaleFactor = (1 - scaleFactor * scaleFactor * (distance / radius) * strength);

                    distance = distance * scaleFactor;
                    disY = (int) (distance * cosA + centerPoint.y + 0.5);
                    disY = checkY(disY, bitmap);
                    disX = (int) (distance * sinA + centerPoint.x + 0.5);
                    disX = checkX(disX, bitmap);
                    //The center point is not processed
                    if (!(j == centerPoint.x && i == centerPoint.y)) {
                        dstBitmap.setPixel(j, i, bitmap.getPixel(disX, disY));
                    }
                }
            }
        }
        TimeAopUtils.end("eye","magnifyEye");
        return dstBitmap;
    }

    private static int checkY(int disY, Bitmap bitmap) {
        if (disY < 0) {
            disY = 0;
        } else if (disY >= bitmap.getHeight()) {
            disY = bitmap.getHeight() - 1;
        }
        return disY;
    }

    private static int checkX(int disX, Bitmap bitmap) {
        if (disX < 0) {
            disX = 0;
        } else if (disX >= bitmap.getWidth()) {
            disX = bitmap.getWidth() - 1;
        }
        return disX;
    }

}
