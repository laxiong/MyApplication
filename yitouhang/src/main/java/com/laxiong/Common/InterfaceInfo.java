package com.laxiong.Common;

public class InterfaceInfo {
	/***
	 * 接口信息
	 */
//	public final static String BASE_URL = "http://licai.gongshidai.com:88/v4_1"; // test  目前不能用
	public final static String BASE_URL = "http://licai.gongshidai.com:88/v4_1";
	
	public final static String USER_URL =BASE_URL + "/user";
	
	public final static String VERIFY_URL = BASE_URL + "/verify";
	
	public final static String CODE_URL = BASE_URL+"/code";
	
	public final static String PRODUCT_URL = BASE_URL +"/product";
	//用户登录
	public final static String LOGIN_URL=BASE_URL+"/login";
	//用户注销
	public final static String LOGINOUT_URL=BASE_URL+"/logout";
	//获取用户信息
	public final static String GETCOUNT_URL=BASE_URL+"/user/";
	//商城
	public final static String SHOP_URL=BASE_URL+"/shoplist";
	//购买
	public final static String SHOPORDER_URL=BASE_URL+"/shoporder";
	//用户红包
	public final static String REDPAPER_URL=BASE_URL+"/luck";
	//添加银行卡
	public final static String BINDCARD_URL=BASE_URL+"/bankcard";
	//我的银行卡
	public final static String MYCARD_URL=BASE_URL+"/bank";
	//银行卡列表
	public final static String BANKLIST_URL=BASE_URL+"/banklist";

}
