package com.laxiong.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.gongshidai.mistGSD.R;
import com.laxiong.InputMethod.LPKeyBoard;
import com.laxiong.InputMethod.LPTextField;
import com.laxiong.InputMethod.LPUtils;

public class BaseActivity extends Activity {

	public InputMethodManager inputManager_;
	public Dialog dlg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //去掉状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);   //去掉标题
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		LPUtils.screenWidth_ = dm.widthPixels;
		LPUtils.screenHeight_ = dm.heightPixels;
		final float w = 320;
		final float h = 480;
		float wRate = dm.widthPixels / w;
		float hRate = dm.heightPixels / h;
	}

	/***
	 * show inputWindows Edtext
	 */
	public void OnCreateInputWindow(final LPTextField edit) {

		LPKeyBoard lpKeyBoard = new LPKeyBoard(this, edit);
		LinearLayout ll = new LinearLayout(this);
		ll.addView(lpKeyBoard);
		dlg = new Dialog(this, R.style.popupAnimation);
		dlg.setContentView(ll);
		// 点击dialog以外的区域关闭dialog
		dlg.setCanceledOnTouchOutside(true);
		lpKeyBoard.dlg_ = dlg;
		// 设置dialog位置
		Window mWindow = dlg.getWindow();
		mWindow.setWindowAnimations(R.style.popupAnimation);
		mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 使对话框位于屏幕的底部并居中
		mWindow.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
		// 使对话框二边没有空隙
		mWindow.setLayout(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		if (!dlg.isShowing()) {
			dlg.show();
			dlg.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					if (edit.isFocused()) {
						edit.clearFocus();
					}
				}
			});
		}
	}

	// 显示正在加载的提示框
	private PopupWindow mShowDialog ;
	private View loadView ;
	public void showLoadingDialog(){

		loadView = LayoutInflater.from(this).inflate(R.layout.show_loading_popwindow,null);
		mShowDialog = new PopupWindow(loadView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT,true);
		mShowDialog.setTouchable(true);
		mShowDialog.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mShowDialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mShowDialog.showAtLocation(loadView, Gravity.BOTTOM, 0, 0);

	}
	// 弄掉显示的提示进度框
	public void dismissShowDialog(){
		if(mShowDialog!=null&&mShowDialog.isShowing()){
			mShowDialog.dismiss();
			mShowDialog = null ;
		}
	}

}
