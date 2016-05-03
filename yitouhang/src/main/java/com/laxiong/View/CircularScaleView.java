package com.laxiong.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class CircularScaleView extends View{
	/***
	 * 自定义的  圆环比例图
	 */
	private int maxProgress = 100;
	private float progressFrist = 25;    //  第一段的progress
	
	private float progressSecond = 75;    //  第二段的progress
	
//	private float progressThird = 30;    //  第三段的progress
	private int progressStrokeWidth = 40;
	private int marxArcStorkeWidth = 40;
	
	private String totalMoney = "263,758";
	
	// 画圆所在的距形区域
	RectF oval;
	Paint paint;
	
	
	public CircularScaleView(Context context, AttributeSet attrs) {
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
		paint.setColor(Color.parseColor("#FFED6139")); // 设置画笔颜色
		canvas.drawColor(Color.TRANSPARENT); // 白色背景
		paint.setStrokeWidth(progressStrokeWidth); // 线宽
		paint.setStyle(Style.STROKE);
		
		oval.left = marxArcStorkeWidth / 2; // 左上角x
		oval.top = marxArcStorkeWidth / 2;  // 左上角y
		oval.right = width - marxArcStorkeWidth / 2; // 左下角x
		oval.bottom = height - marxArcStorkeWidth / 2; // 右下角y
		
		canvas.drawArc(oval, -90, 360, false, paint); // 绘制白色圆圈
		
		paint.setColor(Color.parseColor("#FFED6139"));
		paint.setStrokeWidth(marxArcStorkeWidth);   // 第一部分的圆环
		canvas.drawArc(oval, -90, ((float) progressFrist / maxProgress) * 360,
				false, paint);
		
		paint.setColor(Color.parseColor("#FFFFA71C"));
		paint.setStrokeWidth(marxArcStorkeWidth);	// 第二部分的圆环
		canvas.drawArc(oval, -90+((float) progressFrist / maxProgress) * 360,((float) progressSecond / maxProgress) * 360,
				false, paint);
		
//		paint.setColor(Color.parseColor("#D8D8D8"));
//		paint.setStrokeWidth(marxArcStorkeWidth);
//		canvas.drawArc(oval, -90+((float) progressFrist / maxProgress) * 360+((float) progressSecond / maxProgress) * 360,((float) progressThird / maxProgress) * 360,
//				false, paint);
		
		
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(1);
		String text = "在投资产(元)";
		int textHeight = height / 16;
		paint.setTextSize(textHeight);
		int textWidth = (int) paint.measureText(text, 0, text.length());
		paint.setStyle(Style.FILL);
		canvas.drawText(text, width / 2 - textWidth / 2, height /2
				- textHeight, paint);
		
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		paint.setTextSize(textHeight*2);
		int MoneyWidth = (int) paint.measureText(totalMoney, 0, totalMoney.length());
		paint.setStyle(Style.FILL);
		canvas.drawText(totalMoney, width / 2 - MoneyWidth / 2, height / 2
				+ textHeight , paint);
		
	}

	public float getProgressFrist() {
		return progressFrist;
	}

	public void setProgressFrist(float progressFrist) {
		this.progressFrist = progressFrist;
//		this.setAnimation(playAnimation(-90,((float) progressFrist / maxProgress) * 360));
//		this.invalidate();
	}

	public float getProgressSecond() {
		return progressSecond;
	}

	public void setProgressSecond(float progressSecond) {
		this.progressSecond = progressSecond;
//		if(progressFrist!=0)
//			this.setAnimation(playAnimation(-90+((float) progressFrist / maxProgress) * 360,
//					((float) progressSecond / maxProgress) * 360));
//		this.invalidate();
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
		invalidate();
	}


	//添加动画效果 3秒
	private Animation playAnimation(float fromDegrees, float toDegrees){
		RotateAnimation animation = new RotateAnimation(fromDegrees,
				toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(4);// 设置动画执行时间
		animation.setRepeatCount(1);// 设置重复执行次数
		animation.setFillAfter(true);//设置动画结束后是否停留在结束位置
		return animation;
	}

}
