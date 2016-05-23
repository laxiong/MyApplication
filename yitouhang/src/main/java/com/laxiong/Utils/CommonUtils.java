package com.laxiong.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import java.math.BigDecimal;
import java.net.SocketException;
import java.util.List;

public class CommonUtils {

	// 检查网络状态
	public static boolean checkNetworkState(Context context) throws SocketException {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Activity.CONNECTIVITY_SERVICE);

		State mobileState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		State wifiState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		// 未连接网络
		if (!mobileState.equals(State.CONNECTED) && !mobileState.equals(State.CONNECTING)
				&& !wifiState.equals(State.CONNECTED) && !wifiState.equals(State.CONNECTING)) {
			return false;
		}

		return true;
	}

	public static String getMetaData(Context context, String key) {
		try {
			String data = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).metaData.getString(key);
			return data;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}catch(NullPointerException e){
			e.printStackTrace();
			return "";
		}
	}
	public static boolean isServiceRunning(Context mContext,String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager)
				mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList
				= activityManager.getRunningServices(30);
		if (!(serviceList.size()>0)) {
			return false;
		}
		for (int i=0; i<serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
	public static String calculateByte(long bytenum) {
		//先转换成KB
		if (bytenum > 1024) {
			float kbbyte = (float) bytenum / 1024;
			//转换成M的单位 保留两位小数
			if (kbbyte > 1024) {
				float mbyte = ((float) kbbyte / 1024);
				BigDecimal b = new BigDecimal(mbyte);
				float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
				//转换成G
				if (f1 > 1024) {
					float gbyte = ((float) mbyte / 1024);
					BigDecimal b2 = new BigDecimal(gbyte);
					float f2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
					return f2 + "G";
				} else {
					return f1 + "M";
				}
			} else {
				return Math.round(kbbyte) + "KB";
			}
		} else {
			return Math.round(bytenum) + "B";
		}
	}


}
