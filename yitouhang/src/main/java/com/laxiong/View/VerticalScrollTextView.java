package com.laxiong.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import com.laxiong.entity.Notice;

import java.util.ArrayList;

/**
 * Created by admin on 2016/4/20.
 */
public class VerticalScrollTextView extends TextView {
    /***
     * 竖直方向的滚动的TextView
     */
    private ArrayList<Notice> list;
    private Paint mPaint;
    private Paint mPathPaint;
    private float middleY;
    private int mY;
    private float mX;
    private int index;

    public ArrayList<Notice> getList() {
        return list;
    }
    public void setList(ArrayList<Notice> list) {
        this.list = list;
    }
    public VerticalScrollTextView(Context context) {
        super(context);
        init();
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setFocusable(true);
        //这里主要处理如果没有传入内容显示的默认值
        if(list==null){
            list=new ArrayList<Notice>();
            Notice sen=new Notice(0,"暂时没有通知公告");
            list.add(0, sen);
        }
        //普通文字的字号，以及画笔颜色的设置
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(36);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setTypeface(Typeface.DEFAULT);
        //高亮文字的字号，以及画笔颜色的设置
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setColor(Color.parseColor("#FF999999"));
        mPathPaint.setTextSize(36);
        mPathPaint.setTypeface(Typeface.DEFAULT);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xEFeffff);
        Paint p = mPaint;
        Paint p2 = mPathPaint;
        p.setTextAlign(Paint.Align.CENTER);
        if (index == -1)
            return;
        p2.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(list.get(index).getNotice(), mX, middleY, p2);
        float tempY = middleY;

        for (int i = index - 1; i >= 0; i--) {
//            tempY = tempY - DY;
            if (tempY < 0) {
                break;
            }
            canvas.drawText(list.get(i).getNotice(), mX, tempY, p);
        }
        tempY = middleY;

        for (int i = index + 1; i < list.size(); i++) {
//            tempY = tempY + DY;
            if (tempY > mY) {
                break;
            }
            canvas.drawText(list.get(i).getNotice(), mX, tempY, p);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mX = w * 0.5f;
        mY = h;
        middleY = h * 0.5f;
    }

    private long updateIndex(int index) {
        if (index == -1)
            return -1;
        this.index=index;
        return index;
    }

    public void updateUI(){
        new Thread(new updateThread()).start();
    }

    class updateThread implements Runnable {
        long time = 1000;
        int i=0;
        public void run() {
            while (true) {
                long sleeptime = updateIndex(i);
                time += sleeptime;
                mHandler.post(mUpdateResults);
                if (sleeptime == -1)
                    return;
                try {
                    Thread.sleep(time);
                    i++;
                    if(i==getList().size())
                        i=0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    Handler mHandler = new Handler();
    Runnable mUpdateResults = new Runnable() {
        public void run() {
            invalidate();
        }
    };

}

