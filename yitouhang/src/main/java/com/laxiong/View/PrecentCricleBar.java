package com.laxiong.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2016/4/25.
 */
public class PrecentCricleBar extends View {
    /****
     * 百分百 100%
     */
    private int progressStrokeWidth = 5;
    private int marxArcStorkeWidth = 6;

    private String PaintColor ; //画笔颜色

    public String getPaintColor() {
        return PaintColor;
    }
    public void setPaintColor(String paintColor) {
        PaintColor = paintColor;
    }

    // 画圆所在的距形区域
    RectF oval;
    Paint paint;

    public PrecentCricleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        oval = new RectF();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = this.getWidth();
        int height = this.getHeight();

        width = (width > height) ? height : width;
        height = (width > height) ? height : width;

        paint.setAntiAlias(true);  // 设置画笔为抗锯齿
        paint.setColor(Color.parseColor(PaintColor)); // 设置画笔颜色
        canvas.drawColor(Color.TRANSPARENT); // 白色背景
        paint.setStrokeWidth(progressStrokeWidth); // 线宽
        paint.setStyle(Paint.Style.STROKE);

        oval.left = marxArcStorkeWidth / 2; // 左上角x
        oval.top = marxArcStorkeWidth / 2;  // 左上角y
        oval.right = width - marxArcStorkeWidth / 2; // 左下角x
        oval.bottom = height - marxArcStorkeWidth / 2; // 右下角y

        canvas.drawArc(oval, -90, 360, false, paint); // 绘制白色圆圈

        String text = "磬完";
        int textHeight = height / 4; //设置中间数据的大�?
        paint.setTextSize(textHeight);
        int textWidth = (int) paint.measureText(text, 0, text.length());
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2
                + textHeight / 2, paint);

    }
}
