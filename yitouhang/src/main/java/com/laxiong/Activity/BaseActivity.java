package com.laxiong.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.laxiong.InputMethod.LPKeyBoard;
import com.laxiong.InputMethod.LPTextField;
import com.laxiong.InputMethod.LPUtils;
import com.laxiong.yitouhang.R;

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

}
