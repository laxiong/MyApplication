package com.laxiong.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by xiejin on 2016/5/6.
 * Types ImageUtil.java
 */
public class ImageUtil {
    public static Bitmap reverse(Bitmap bm,float degree){
        Matrix matrix=new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,false);
    }
}
