package com.laxiong.Common;

public class InterfaceInfo {
	/***
	 * 接口信息
	 */
//	public final static String BASE_URL = "http://licai.gongshidai.com:88/v4_1"; // test  目前不能用
//	public final static String BASE_URL = "http://licai.gongshidai.com:88/v4_1";
	public final static String XIANSHAGN_URL="http://licai.gongshidai.com/v4_1";
	public final static String BASE_URL="https://www.cheyoulicai.com/v4_1";
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
	//提现
	public final static String WITHDRAW_URL=BASE_URL+"/withdraw";
	//资金流水
	public final static String FUND_URL=BASE_URL+"/fund/";
	//消息
	public final static String NOTICE_URL=BASE_URL+"/notice";
	//积分
	public final static String SCORE_URL=BASE_URL+"/score/";
	//欢迎
	public final static String WELCOME_URL=BASE_URL+"/start";
	//活动大厅
	public final static String ACTIVITY_URL=BASE_URL+"/activity";
	//邀请好友
	public final static String INVITE_URL="https://licai.gongshidai.com/wap/public/desc/my.invite.html?";
	//人脉规则说明
	public final static String RENMAIEXP_URL="https://licai.gongshidai.com/wap/public/desc/reward.rule.html";
	//人脉数量
	public final static String RMNUM_URL=BASE_URL+"/friendsnum";
	//人脉详情
	public final static String FRIENDS_URL=BASE_URL+"/friends";
	//分享
	public final static String SHARE_URL=BASE_URL+"/share";
	//版本更新
	public final static String UPDATE_URL="http://a.app.qq.com/o/simple.jsp?pkgname=com.gongshidai.mistGSD";
	//统计登录
	public final static String RECORDLOGIN_URL=BASE_URL+"/appLogin";
	//风险评估
	public final static String EVALUATE_URL="https://licai.gongshidai.com/wap/public/ytbank/yt.assess.html";
	//软件开关
	public final static String VERSION_URL=BASE_URL+"/switch";
	//用户反馈
	public final static  String ADVICE_URL=BASE_URL+"/advice";
	//首页广告(非banner)
	public final static String FIRSTAD_URL=BASE_URL+"/adlist";
	//检查更新
	public final static String CKUPDATE_URL=XIANSHAGN_URL+"/checkUp";
	//订单
	public final static String RDETAIL_URL=BASE_URL+"/receipts";
	//判断身份证
	public final static String JGIDC_URL=BASE_URL+"/isIdc";
	//判断验证码
	public final static String JGCODE_URL=BASE_URL+"/isCode";
	//通联支付购买产品
	public final static String TLBUY_URL= BASE_URL+"/tlbuy";
	//通联支付充值
	public final static String TLPAY_URL=BASE_URL+"/tlpay";
	//验证支付密码的单独借口
	public final static String verifyPay_URL=BASE_URL+"/verify";
}
