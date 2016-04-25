package com.laxiong.Common;

import android.widget.ImageView;

import com.laxiong.yitouhang.R;
import com.loopj.android.http.Base64;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

	public final static String sharedPrefName = "YITOUHANG";

	static boolean isChecked = false ;
	static boolean isShowed = false ;
	
	//TODO 阅读协议的 Bug
	public static void isCheck(ImageView iv ){

		if(isChecked){ // 是阅读的
			iv.setImageResource(R.drawable.img_read);
			isChecked = false ;
		}else{  // 没有阅读
			iv.setImageResource(R.drawable.img_no_read);
			isChecked = true ;
		}
	}

	// 64位编码的加密编码
	public static String authorizeStr(int id, String token) {
		return new String(Base64.encode((id + ":" + token).getBytes(), Base64.NO_WRAP));
	}

	// 验证手机号码
	public static boolean isMobileNO(String mobiles){
		
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		Matcher m = p.matcher(mobiles);
		return m.matches();
		
	}
	// 输入框的内容的非空判断
	public static boolean inputContentNotNull(String str){
		
		if(str!=null&&str.length()!=0&&!str.equals("")){
			return true ;
		}else{
			return false ;
		}
	}
	
	// 判断输入的密码是不是  至少是6位的
	public static boolean inputPswdCount(String pswd){

		Pattern pp = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,24}$");
		Matcher m = pp.matcher(pswd);
		return m.matches();
	}
	
	
}
