package com.devramisha.handsomelook.makeup;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devramisha.handsomelook.R;
import com.devramisha.handsomelook.makeup.beauty.MagnifyEyeUtils;
import com.devramisha.handsomelook.makeup.utils.CommonShareBitmap;
import com.devramisha.handsomelook.makeup.utils.FacePoint;

import java.util.Objects;

/**
 * Developer :- Ramish
 * time:2021-08
 * function: default function
 */
public class MagnifyActivity extends AppCompatActivity {

    private ImageView img;
    private ImageView imgResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

        imgResult = findViewById(R.id.imgResult);
        img = findViewById(R.id.img);

        loadImage();
    }

    private void loadImage() {
        img.setImageBitmap(CommonShareBitmap.originBitmap);
        Bitmap result = CommonShareBitmap.originBitmap.copy(Bitmap.Config.RGB_565,true);

        String faceJson = FacePoint.getFaceJson(this,"face_point1.json");
        Bitmap magnifyLeftEye = MagnifyEyeUtils.magnifyEye(CommonShareBitmap.originBitmap,
                Objects.requireNonNull(FacePoint.getLeftEyeCenter(faceJson)),
                FacePoint.getLeftEyeRadius(faceJson) * 4,
                3);

        Bitmap magnifyEye = MagnifyEyeUtils.magnifyEye(magnifyLeftEye,
                Objects.requireNonNull(FacePoint.getRightEyeCenter(faceJson)),
                FacePoint.getLeftEyeRadius(faceJson) * 4,
                3);

        magnifyLeftEye.recycle();

        imgResult.setImageBitmap(magnifyEye);

    }
}
