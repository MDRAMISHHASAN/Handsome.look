package com.devramisha.handsomelook.makeup.beauty;

/**
 * @author by dingdegao
 * time 2017/10/12 18:21
 * function:
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Developer :- Ramish
 * time:2021-08
 * function: default function
 */
public class SmallFaceView extends View {

    private int mWidth, mHeight;//View Width and height

    //Radius of action
    private int r = 160;

    //Scope of circle drawing
    private float radius = 50;

    private Paint circlePaint;
    private Paint directionPaint;

    //Whether to show the deformed circle
    private boolean showCircle;
    //Whether to show the direction of deformation
    private boolean showDirection;

    //Deformation start coordinates, sliding coordinates
    private float startX, startY, moveX, moveY;

    //How many grids to divide the image
    private int WIDTH = 200;
    private int HEIGHT = 200;

    //Number of intersection coordinates
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);

    //Used to save the coordinates of COUNT
    //x0, y0, x1, y1......
    private float[] verts = new float[COUNT * 2];

    //Used to save the original coordinates
    private float[] orig = new float[COUNT * 2];

    private Bitmap mBitmap;

    private boolean isEnableOperate = true;
    private float mScale = 1.0f;
    private int dx = 0;
    private int dy = 0;


    private IOnStepChangeListener onStepChangeListener;

    public SmallFaceView(Context context) {
        super(context);
        init();
    }

    public SmallFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmallFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(Color.parseColor("#FFE130"));
        circlePaint.setAntiAlias(true);

        directionPaint = new Paint();
        directionPaint.setStyle(Paint.Style.FILL);
        directionPaint.setStrokeWidth(5);
        directionPaint.setColor(Color.parseColor("#FFE130"));
        directionPaint.setAntiAlias(true);

        radius = 100;
    }

    public void setEnableOperate(boolean enableOperate) {
        isEnableOperate = enableOperate;
    }


    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        if(bitmap == null) return;
        post(new Runnable() {
            @Override
            public void run() {
                zoomBitmap(mBitmap,getWidth(),getHeight());
                invalidate();
            }
        });
        invalidate();
    }

    public void setRestoreBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        invalidate();
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public Bitmap getBitmap() {
        if(mBitmap == null) return null;
        Bitmap copy = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(copy);
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        return copy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public float getScale(){
        return mScale;
    }


    private void restoreVerts() {
        int index = 0;
        float bmWidth = mBitmap.getWidth();
        float bmHeight = mBitmap.getHeight();
        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bmHeight * i / HEIGHT;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bmWidth * j / WIDTH;
                //X-axis coordinates are placed in even digits
                verts[index * 2] = fx;
                orig[index * 2] = verts[index * 2];
                //The Y-axis coordinates are placed in odd places
                verts[index * 2 + 1] = fy;
                orig[index * 2 + 1] = verts[index * 2 + 1];
                index += 1;
            }
        }
        showCircle = false;
        showDirection = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void zoomBitmap(Bitmap bitmap, int width, int height) {
        if(bitmap == null) return;
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();

        float scale = 1.0f;

        // The width of the picture is greater than the width of the control,
        // the height of the picture is less than the height of the space,
        // we will reduce it
        if (dw > width && dh < height) {
            scale = width * 1.0f / dw;
        }

        // The width of the picture is smaller than the width of the control,
        // the height of the picture is greater than the height of the space,
        // we will reduce it
        if (dh > height && dw < width) {
            scale = height * 1.0f / dh;
        }

        // Reduced value
        if (dw > width && dh > height) {
            scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
        }

        // Magnification value
        if (dw < width && dh < height) {
            scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
        }

        //Zoom out
        if (dw == width && dh > height) {
            scale = height * 1.0f / dh;
        }
        dx = width / 2 - (int) (dw * scale + 0.5f) / 2;
        dy = height / 2 - (int) (dh * scale + 0.5f) / 2;

        mScale = scale;
        restoreVerts();
    }

    public void setLevel(int level) {
        //level [0,4]
        r = 140 + 15 * level;
        radius = 100;
        invalidate();
    }

    boolean isSmllBody = false;

    public void setSmllBody(boolean isSmallBody) {
        isSmllBody = isSmallBody;
    }

    boolean isShowOrigin = false;

    public void showOrigin(boolean isShowOrigin) {
        this.isShowOrigin = isShowOrigin;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap == null) return;
        canvas.save();
        canvas.translate(dx, dy);
        canvas.scale(mScale, mScale);
        if (isShowOrigin) {
            canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, orig, 0, null, 0, null);
        } else {
            canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        }

        canvas.restore();
        if (showCircle && isEnableOperate) {
            canvas.drawCircle(startX, startY, radius, circlePaint);
            canvas.drawCircle(startX, startY, 5, directionPaint);
        }
        if (showDirection && isEnableOperate) {
            canvas.drawLine(startX, startY, moveX, moveY, directionPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnableOperate) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Draw the deformed area
                startX = event.getX();
                startY = event.getY();
                showCircle = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                //Draw deformation direction
                moveX = event.getX();
                moveY = event.getY();
                showDirection = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                showCircle = false;
                showDirection = false;

                //Call the warp method to warp the verts array according to the coordinate
                // points of the touch screen event
                if(mBitmap != null && verts!= null && !mBitmap.isRecycled()) {
                    warp(startX, startY, event.getX(), event.getY());
                }

                if (onStepChangeListener != null) {
                    onStepChangeListener.onStepChange(false);
                }
                break;
        }
        return true;
    }

    /**
     * Convert the screen touch coordinate x to the coordinate in the picture
     */
    public final float toX(float touchX) {
        return (touchX  - dx) /  mScale;
    }

    /**
     * Convert the screen touch coordinate y to the coordinate in the picture
     */
    public final float toY(float touchY) {
        return (touchY  - dy) /  mScale;
    }

    private void warp(float startX, float startY, float endX, float endY) {
        startX = toX(startX);
        startY = toY(startY);
        endX = toX(endX);
        endY = toY(endY);

        //Calculate drag distance
        float ddPull = (endX - startX) * (endX - startX) + (endY - startY) * (endY - startY);
        float dPull = (float) Math.sqrt(ddPull);
        //dPull = screenWidth - dPull >= 0.0001f ? screenWidth - dPull : 0.0001f;
        if (dPull < 2 * r) {
            if (isSmllBody) {
                dPull = 1.8f * r;
            } else {
                dPull = 2.5f * r;
            }
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

                   // check
                    if(verts[index * 2] < 0){
                        verts[index * 2] = 0;
                    }
                    if(verts[index * 2] > mBitmap.getWidth()){
                        verts[index * 2] =  mBitmap.getWidth();
                    }

                    if(verts[index * 2 + 1] < 0){
                        verts[index * 2 +1] = 0;
                    }
                    if(verts[index * 2 + 1] > mBitmap.getHeight()){
                        verts[index * 2 + 1] = mBitmap.getHeight();
                    }
                }
                index = index + 1;
            }
        }
        invalidate();
    }

    /**
     * One-click recovery
     */

    public void resetView() {
        for (int i = 0; i < verts.length; i++) {
            verts[i] = orig[i];
        }
        if (onStepChangeListener != null) {
            onStepChangeListener.onStepChange(true);
        }
        showCircle = false;
        showDirection = false;
        invalidate();
    }

    public void setOnStepChangeListener(IOnStepChangeListener onStepChangeListener) {
        this.onStepChangeListener = onStepChangeListener;
    }

    public interface IOnStepChangeListener {
        void onStepChange(boolean isEmpty);
    }

}

