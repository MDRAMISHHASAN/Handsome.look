package com.devramisha.handsomelook.makeup.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Developer :- Ramish
 * time:2021-09
 * function: default function
 */

public class BlushDraw {

    public static void drawBlush(Canvas canvas, Bitmap faceBlush, Path path, int alpha) {
        Paint paint = new Paint();
        paint.setAlpha(alpha);

        RectF rectF = new RectF();
        path.computeBounds(rectF,true);

        canvas.drawBitmap(faceBlush,null,rectF,paint);

    }
}
