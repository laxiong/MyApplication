package com.laxiong.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gongshidai.mistGSD.R;

/**
 * Created by xiejin on 2016/5/17.
 * Types WaitPgView.java
 */
public class WaitPgView extends View{
    private int firstColor;
    private int secondColor;
    private String text;
    private float strokewidth;
    private boolean flag=true;
    private float degree=0;
    private boolean isFirst=true;
    private Rect rect;
    private float textsize;
    public WaitPgView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public WaitPgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray tary=context.obtainStyledAttributes(attrs,R.styleable.WaitPgView);
        int count=tary.getIndexCount();
        for(int i=0;i<count;i++){
            switch (tary.getIndex(i)){
                case R.styleable.WaitPgView_firstColor:
                    firstColor=tary.getColor(R.styleable.WaitPgView_firstColor,context.getResources().getColor(R.color.divider_grey));
                    break;
                case R.styleable.WaitPgView_secondColor:
                    secondColor=tary.getColor(R.styleable.WaitPgView_secondColor,context.getResources().getColor(R.color.light_green));
                    break;
                case R.styleable.WaitPgView_circletext:
                    text=tary.getString(R.styleable.WaitPgView_circletext);
                    break;
                case R.styleable.WaitPgView_strokewidth:
                    strokewidth=tary.getDimensionPixelSize(R.styleable.WaitPgView_strokewidth,5);
                    break;
                case R.styleable.WaitPgView_tsize:
                    textsize=tary.getDimensionPixelSize(R.styleable.WaitPgView_tsize,13);
                    break;
            }
        }
        tary.recycle();
        new Thread(){
            @Override
            public void run() {
                while(flag){
                    degree+=2;
                    if(degree==360){
                        isFirst=!isFirst;
                        degree=0;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        rect=new Rect();
        Paint paint=new Paint();
        paint.getTextBounds(text,0,text.length(),rect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint pfirst=new Paint();
        pfirst.setStyle(Paint.Style.STROKE);
        pfirst.setColor(isFirst ? firstColor : secondColor);
        pfirst.setAntiAlias(true);
        pfirst.setStrokeWidth(strokewidth);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2
                , getWidth() / 2 - getPaddingLeft() - strokewidth / 2, pfirst);
        Paint psecond=new Paint();
        psecond.setStyle(Paint.Style.STROKE);
        psecond.setColor(isFirst ? secondColor : firstColor);
        psecond.setAntiAlias(true);
        psecond.setStrokeWidth(strokewidth);
        canvas.drawArc(new RectF(getPaddingLeft() + strokewidth / 2, getPaddingTop() + strokewidth / 2, getWidth() - strokewidth / 2, getHeight() - strokewidth / 2), -90, degree, false, psecond);
        Paint ptext=new Paint();
        ptext.setColor(Color.GRAY);
        ptext.setAntiAlias(true);
        ptext.setTextSize(textsize);
        ptext.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text,getWidth()/2,getHeight()/2+rect.height()/2,ptext);
    }
}
