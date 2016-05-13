package com.laxiong.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

import com.gongshidai.mistGSD.R;

/**
 * Created by xiejin on 2016/5/10.
 * Types CustomFlipper.java
 */
public class CustomFlipper extends ViewFlipper implements GestureDetector.OnGestureListener {
    private Animation leftInAnimation, rightInAnimation, leftOutAnimation, rightOutAnimation;
    private GestureDetector mydector;
    private int lastposition = 0, nowposition = 0;
    private InterFlipperAd interflipper;
    private static final int MIN_DISTANCE = 20;

    public interface InterFlipperAd {
        void changePoint(int position, boolean flag);

        int getListSize();

        void onItemClick(int position);
    }

    public void setListener(InterFlipperAd interflipper) {
        this.interflipper = interflipper;
    }

    ;

    public CustomFlipper(Context context) {
        super(context);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return mydector.onTouchEvent(event);
    }

    @Override
    public void showNext() {
        nowposition++;
        nowposition %= interflipper.getListSize();
        changePoint();
        this.setInAnimation(leftInAnimation);
        this.setOutAnimation(leftOutAnimation);
        super.showNext();
    }

    public void changePoint() {
        if (interflipper == null)
            return;
        if (lastposition != nowposition)
            interflipper.changePoint(lastposition, false);
        interflipper.changePoint(nowposition, true);
        lastposition = nowposition;
    }

    @Override
    public void showPrevious() {
        nowposition--;
        int size = interflipper.getListSize();
        if (nowposition == -1)
            nowposition = size - 1;
        nowposition %= size;
        changePoint();
        this.setInAnimation(rightInAnimation);
        this.setOutAnimation(rightOutAnimation);
        super.showPrevious();
    }

    public CustomFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        leftInAnimation = AnimationUtils.loadAnimation(context, R.anim.left_in);
        leftOutAnimation = AnimationUtils.loadAnimation(context, R.anim.left_out);
        rightInAnimation = AnimationUtils.loadAnimation(context, R.anim.right_in);
        rightOutAnimation = AnimationUtils.loadAnimation(context, R.anim.right_out);
        mydector = new GestureDetector(context, this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    public GestureDetector getDector() {
        return mydector;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        interflipper.onItemClick(nowposition);
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() >= MIN_DISTANCE) {
            this.showNext();
            return true;
        } else if (e1.getX() - e2.getX() <= -MIN_DISTANCE) {
            this.showPrevious();
            return true;
        }
        return false;
    }
}
