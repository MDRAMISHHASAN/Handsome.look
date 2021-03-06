package com.devramisha.handsomelook.makeup.utils;

import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;

import java.util.Comparator;
import java.util.List;

/**
 * @author by Md Ramish
 *         time 2021/9
 *         function: Eye angle, zoom ratio calculation
 */

public class EyeAngleAndScaleCalc {

    public Point topP1;
    private Point topP2;
    private Point topP3;
    public Point bottomP1;
    private Point bottomP2;
    private Point bottomP3;

    public double topScaleX = 1.0f;
    public double topScaleY = 1.0f;

    public double bottomScaleX = 1;
    public double bottomScaleY = 1;

    private List<Point> mPointList;
    private Bean bean;

    public EyeAngleAndScaleCalc(List<Point> pointList, Bean bean){
        this.bean = bean;
        this.mPointList = pointList;

        topP1 = new Point(pointList.get(0).x,pointList.get(1).y);
        topP2 = getCenter(pointList, 14, 18);
        topP3 = pointList.get(31);

        if(!TextUtils.isEmpty(bean.resBottom)) {
            bottomP1 = pointList.get(62);
            bottomP2 = getCenter(pointList, 44, 48);
            bottomP3 = pointList.get(32);
        }
        topScaleX = computeScaleX(topP1, topP3, bean.topP1, bean.topP3);
        topScaleY = computeScaleY(topP1, this.topP2, topP3, bean.topP1, bean.topP2, bean.topP3);

        if(!TextUtils.isEmpty(bean.resBottom)) {
            bottomScaleX = computeScaleX(bottomP1, bottomP3, bean.bottomP1, bean.bottomP3);
            bottomScaleY = computeScaleY(bottomP1, bottomP2, bottomP3, bean.bottomP1, bean.bottomP2, bean.bottomP3);
        }
    }

    public float getTopEyeAngle(){
        float bgEyeAngle = (float) getAngle(topP1, topP3, new Point(topP1.x, topP3.y));
        if (topP1.y > topP3.y) {
            bgEyeAngle = -1 * bgEyeAngle;
        }
        return bgEyeAngle;
    }


    public float getBottomEyeAngle(){
        float bgEyeAngle = (float) getAngle(bottomP1, bottomP3, new Point(bottomP1.x, bottomP3.y));
        if (bottomP1.y > bottomP3.y) {
            bgEyeAngle = -1 * bgEyeAngle;
        }
        return bgEyeAngle;
    }

    public Point getCenter(List<Point> list, int start, int end) {
        float x = 0.0f,y=0.0f;
        for(int i=start;i<=end;i++){
            x += list.get(i).x;
            y += list.get(i).y;
        }
        return new Point((int)(x/(end-start)),(int)(y/(end-start)));
    }

    /**
     * @param targetP1 Zoom target line segment point p1
     * @param targetP2 Zoom target line segment point p2
     * @param P1        Line segment point to be scaled p1
     * @param P2       Line segment point to be scaled p2
     * @return Horizontal height ratio
     */

    public double computeScaleX(Point targetP1, Point targetP2, Point P1, Point P2) {
        int targetLengthSquare = (targetP1.x - targetP2.x) * (targetP1.x - targetP2.x) +
                (targetP1.y - targetP2.y) * (targetP1.y - targetP2.y);
        int sourceLengthSquare = (P1.x - P2.x) * (P1.x - P2.x) + (P1.y - P2.y) * (P1.y - P2.y);
        double scale = targetLengthSquare * 1.0 / sourceLengthSquare;
        return Math.sqrt(scale);
    }

    /**
     * @param targetP1 Zoom target triangle vertex
     * @param targetP2 Zoom target triangle vertex
     * @param targetP3 Zoom target triangle vertex
     * @param P1       Vertex of triangle to be scaled
     * @param P2       Vertex of triangle to be scaled
     * @param P3       Vertex of triangle to be scaled
     * @return Vertical height ratio
     */

    public double computeScaleY(Point targetP1, Point targetP2, Point targetP3, Point P1, Point P2, Point P3) {
        double targetHeight = getTriangleHeight(targetP1, targetP2, targetP3);
        double sourceHeight = getTriangleHeight(P1, P2, P3);
        return targetHeight / sourceHeight;
    }

    /**
     * @param p1 Triangle vertex
     * @param p2 Triangle vertex
     * @param p3 Triangle vertex
     * @return Vertical height of triangle vertices p3 to p1,p3
     */

    public double getTriangleHeight(Point p1, Point p2, Point p3) {
        int a = p1.x;
        int b = p1.y;
        int c = p2.x;
        int d = p2.y;
        int e = p3.x;
        int f = p3.y;
        //Calculate the area of a triangle
        double S = (a * d + b * e + c * f - a * f - b * c - d * e) / 2;
        int lengthSquare = (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
        return Math.abs(2 * S / Math.sqrt(lengthSquare));
    }

    static double pi180 = 180 / Math.PI;

    public double getAngle(Point p1, Point p2, Point p3) {
        double _cos1 = getCos(p1, p2, p3);//The first point is the cosine of the angle of the vertex
        return 90 - Math.acos(_cos1) * pi180;
    }

    //Obtain the cosine value of the angle at the first point of the triangle formed by three points
    public double getCos(Point p1, Point p2, Point p3) {
        //Get the distance between the first point and the second point
        double length1_2 = getLength(p1, p2);
        double length1_3 = getLength(p1, p3);
        double length2_3 = getLength(p2, p3);
        //cosA=(pow(b,2)+pow(c,2)-pow(a,2))/2*b*c
        double res = (Math.pow(length1_2, 2) + Math.pow(length1_3, 2) - Math.pow(length2_3, 2)) / (length1_2 * length1_3 * 2);
        return res;
    }

    //Get the distance between two points in the coordinate axis
    public double getLength(Point p1, Point p2) {
        double diff_x = Math.abs(p1.x - p2.x);
        double diff_y = Math.abs(p1.y - p2.y);
        //The difference between the abscissa and ordinate of the two points
        // and the straight line between the two points form a right triangle.
        // length_pow is equal to the square of the distance
        double length_pow = Math.pow(diff_x, 2) + Math.pow(diff_y, 2);
        double sqrt = Math.sqrt(length_pow);
        return sqrt == 0?0.001f:(float) sqrt;
    }

    public static class Bean implements Comparator<Bean> {

        public Point topP1;
        public Point topP2;
        public Point topP3;
        public Point bottomP1;
        public Point bottomP2;
        public Point bottomP3;


        public String resTop;
        public String resBottom;

        public Rect rect;

        public Bean() {
        }

        @Override
        public int compare(Bean o1, Bean o2) {
            if (o1 == null) {
                if (o2 == null) {
                    return 0;
                }
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return 0;
        }


    }

}
