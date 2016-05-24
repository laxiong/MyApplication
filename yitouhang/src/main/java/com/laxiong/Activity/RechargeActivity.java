package com.laxiong.Activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongshidai.mistGSD.R;
import com.google.gson.GsonBuilder;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Common;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Utils.BaseHelper;
import com.laxiong.Utils.Constants;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.Md5Algorithm;
import com.laxiong.Utils.MobileSecurePayer;
import com.laxiong.entity.EnvConstants;
import com.laxiong.entity.LlOrderInfo;
import com.laxiong.entity.PayOrder;
import com.laxiong.entity.PaySignParam;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class RechargeActivity extends BaseActivity implements View.OnClickListener {
	/***
	 * 充值页面
	 */
	private LinearLayout mPayMethod;
	private FrameLayout mBack;
	private TextView mRechargeBtn, mPayBank;
	private ImageView mToggleBtn;
	private EditText mInputRecharge; //输入的支付金额
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		initView();
		getBankId();
	}

	private void initView() {
		mPayMethod = (LinearLayout) findViewById(R.id.change_pay_method);
		mBack = (FrameLayout) findViewById(R.id.back_layout);
		mRechargeBtn = (TextView) findViewById(R.id.rechargeBtn);
		mToggleBtn = (ImageView) findViewById(R.id.toggle_img);
		mPayBank = (TextView) findViewById(R.id.pay_bank); // 选择的银行卡显示的内容
		mInputRecharge = (EditText) findViewById(R.id.input_recharge);

		mBack.setOnClickListener(this);
		mPayMethod.setOnClickListener(this);
		mRechargeBtn.setOnClickListener(this);
		mToggleBtn.setOnClickListener(this);
		mInputRecharge.addTextChangedListener(watcher);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.change_pay_method:
				payMenthodType();
				break;
			case R.id.back_layout:
				this.finish();
				break;
			case R.id.rechargeBtn:
				getOrderInfo();
				break;
			case R.id.toggle_img:
				readProcotol();
				break;
		}
	}

	// 阅读协议
	private boolean isRead = false;

	private void readProcotol() {
		if (isRead) { // 是阅读的
			mToggleBtn.setImageResource(R.drawable.img_read);
			isRead = false;
		} else {  // 没有阅读
			mToggleBtn.setImageResource(R.drawable.img_no_read);
			isRead = true;
		}
		valify();
	}

	TextWatcher watcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		@Override
		public void afterTextChanged(Editable s) {
			valify();
		}
	};
	private void valify(){
		if (!TextUtils.isEmpty(mInputRecharge.getText().toString().trim())&&!isRead){
			mRechargeBtn.setEnabled(true);
			mRechargeBtn.setBackgroundResource(R.drawable.button_change_bg_border);
		}else {
			mRechargeBtn.setEnabled(false);
			mRechargeBtn.setBackgroundResource(R.drawable.button_grey_corner_border);
		}
	}
	// pay Menthod
	private PopupWindow mPayMathodWindow;
	private View PayView;
	private ImageView newCard_img, constranceBank_img ,BankIconPop , NewCardIconPop;
	private TextView mBankNamePop,mNewCardPop ;

	private void payMenthodType() {

		PayView = LayoutInflater.from(RechargeActivity.this).inflate(R.layout.pay_recharge_mathod_popwindow, null);

		RelativeLayout newCard = (RelativeLayout) PayView.findViewById(R.id.addnewcard);
		RelativeLayout constranceBank = (RelativeLayout) PayView.findViewById(R.id.concreatebank);
		TextView mConcel = (TextView) PayView.findViewById(R.id.concel);
//		TextView bankname = (TextView) PayView.findViewById(R.id.bankname);
//		bankname.setText(getmBankName()+"(尾号"+bankLastNum+")");

		newCard_img = (ImageView) PayView.findViewById(R.id.change_img_addnewcard);
		constranceBank_img = (ImageView) PayView.findViewById(R.id.change_img_concreatebank);

		newCard.setOnClickListener(listenner);
		constranceBank.setOnClickListener(listenner);
		mConcel.setOnClickListener(listenner);

		//银行卡等信息
		mBankNamePop =(TextView)PayView.findViewById(R.id.bankname);
		mNewCardPop =(TextView)PayView.findViewById(R.id.newcard);
		//Item卡的图片
		BankIconPop =(ImageView)PayView.findViewById(R.id.icon1);
		NewCardIconPop =(ImageView)PayView.findViewById(R.id.icon3);

		String nameStr = getmBankName();
		mBankNamePop.setText(nameStr+"(尾号"+bankLastNum+")");

		if (logokey!=null)
			BankIconPop.setImageResource(getResources().getIdentifier("logo_" + logokey, "drawable", getPackageName()));

		mPayMathodWindow = new PopupWindow(PayView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
		mPayMathodWindow.setTouchable(true);
		mPayMathodWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		mPayMathodWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
		mPayMathodWindow.showAtLocation(PayView, Gravity.BOTTOM, 0, 0);
	}

	View.OnClickListener listenner = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			initNoSelect();
			switch (v.getId()) {
				case R.id.addnewcard:  // 加新卡
					newCard_img.setImageResource(R.drawable.img_read);
					break;
				case R.id.concreatebank:  // 建设银行
					constranceBank_img.setImageResource(R.drawable.img_read);

					break;
				case R.id.concel:
					if (mPayMathodWindow != null && mPayMathodWindow.isShowing()) {
						mPayMathodWindow.dismiss();
						mPayMathodWindow = null;
					}
					break;
			}
		}
	};

	private void initNoSelect(){
		newCard_img.setImageResource(R.drawable.img_no_read);
		constranceBank_img.setImageResource(R.drawable.img_no_read);
	}

	// 获取订单信息
	private void getOrderInfo() {
		RequestParams params = new RequestParams();
		params.put("amount", mInputRecharge.getText().toString().trim());
		params.put("bank_id", bankId);

		Log.i("WKKKKK", "连连支付的权限：" + Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
				.getUserLogin().getToken()));

		HttpUtil.get(InterfaceInfo.BASE_URL + "/llpay", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response != null) {
					try {

						if (response.getInt("code") == 0) {
							Log.i("WK", "连连支付" + response);
							LlOrderInfo mInfo  = new GsonBuilder().create().fromJson(response.toString(),
									LlOrderInfo.class);
							PayOrder mPayOrder = constructPreCardPayOrder(mInfo);
							String content4Pay = BaseHelper.toJSONString(mPayOrder);
							// 关键 content4Pay
							// 用于提交到支付SDK的订单支付串，如遇到签名错误的情况，请将该信息帖给我们的技术支持
							MobileSecurePayer msp = new MobileSecurePayer();
							boolean bRet = msp.pay(content4Pay, mHandler, Constants.RQF_PAY,
									RechargeActivity.this, false);

							Toast.makeText(RechargeActivity.this, "成功", Toast.LENGTH_SHORT).show();

						} else {
							if (!TextUtils.isEmpty(response.getString("msg"))) {
								Toast.makeText(RechargeActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(RechargeActivity.this, "获取订单信息失败", Toast.LENGTH_SHORT).show();
							}
						}
					} catch (Exception E) {
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		}, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
				.getUserLogin().getToken()));

	}
	private String logokey ;
	private int bankLastNum ;
	// 获取银行卡id
	private void getBankId() {
		Log.i("WKKKKK", "权限：" + Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
				.getUserLogin().getToken()));
		HttpUtil.get(InterfaceInfo.BASE_URL + "/bank", new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (response != null) {
					try {
						Log.i("WKKKKKK", "我的一张银行卡：" + response);
						if (response.getInt("code") == 0) {
							setBankId(response.getInt("id"));
							setmBankName(response.getString("name"));
							setmCardNum(response.getString("number"));
							logokey = response.getString("logoKey");
							mPayBank.setText(response.getString("name")+"(尾号"+response.getInt("snumber")+")");
							bankLastNum = response.getInt("snumber");
						} else {
							Toast.makeText(RechargeActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
						}
					} catch (Exception E) {
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(RechargeActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
			}
		}, Common.authorizeStr(YiTouApplication.getInstance().getUserLogin().getToken_id(), YiTouApplication.getInstance()
				.getUserLogin().getToken()));

	}

	private int bankId = -1;
	private String mBankName = "我的余额";
	private String mCardNum = "00";

	public String getmCardNum() {
		return mCardNum;
	}

	public void setmCardNum(String mCardNum) {
		this.mCardNum = mCardNum;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getmBankName() {
		return mBankName;
	}

	public void setmBankName(String mBankName) {
		this.mBankName = mBankName;
	}
	//构造订单类
	private PayOrder constructPreCardPayOrder(LlOrderInfo mInfo) {

		PayOrder order = new PayOrder();
		PaySignParam signParam = new PaySignParam();
		signParam.setBusi_partner(mInfo.getBusi_partner());
		signParam.setNo_order(mInfo.getNo_order());
		signParam.setDt_order(mInfo.getDt_order());
		signParam.setName_goods(mInfo.getName_goods());
		signParam.setNotify_url(mInfo.getNotify_url());
		signParam.setSign_type(PayOrder.SIGN_TYPE_MD5);
		signParam.setValid_order(mInfo.getValid_order());
		signParam.setInfo_order(mInfo.getInfo_order());
		signParam.setMoney_order(mInfo.getMoney_order());
		// 银行卡历次支付时填写，可以查询得到，协议号匹配会进入SDK，
		// order.setNo_agree();
		// 风险控制参数
		signParam.setRisk_item(mInfo.getRisk_item());
		signParam.setOid_partner(EnvConstants.PARTNER);
		String sign = "";
		String content = BaseHelper.sortParam(signParam);
		// MD5 签名方式
		sign = Md5Algorithm.getInstance().sign(content, EnvConstants.MD5_KEY);

		order.setSign(sign);

		order.setBusi_partner(mInfo.getBusi_partner());
		order.setNo_order(mInfo.getNo_order());
		order.setDt_order(mInfo.getDt_order());
		order.setName_goods(mInfo.getName_goods());
		order.setNotify_url(mInfo.getNotify_url());
		order.setSign_type(PayOrder.SIGN_TYPE_MD5);
		order.setValid_order(mInfo.getValid_order());

		order.setUser_id(mInfo.getUser_id());
		order.setId_no(mInfo.getId_no());
		order.setInfo_order(mInfo.getInfo_order());

		order.setAcct_name(mInfo.getAcct_name());
		order.setMoney_order(mInfo.getMoney_order());
		order.setNo_goods(mInfo.getNo_goods());

		// 银行卡卡号，该卡首次支付时必填
		order.setCard_no(mInfo.getCard_no());
		// 银行卡历次支付时填写，可以查询得到，协议号匹配会进入SDK，
		// order.setNo_agree();

		// int id = ((RadioGroup)
		// findViewById(R.id.flag_modify_group)).getCheckedRadioButtonId();
		// if (id == R.id.flag_modify_0) {
		// order.setFlag_modify("0");
		// } else if (id == R.id.flag_modify_1) {
		// order.setFlag_modify("1");
		// }
		// 风险控制参数
		order.setRisk_item(mInfo.getRisk_item());
		order.setOid_partner(EnvConstants.PARTNER);

		return order;
	}

	private Handler mHandler = createHandler();
	@SuppressLint("HandlerLeak")
	private Handler createHandler() {
		return new Handler() {
			public void handleMessage(Message msg) {
				String strRet = (String) msg.obj;
				switch (msg.what) {
					case Constants.RQF_PAY: {
						JSONObject objContent = BaseHelper.string2JSON(strRet);
						String retCode = objContent.optString("ret_code");
						String retMsg = objContent.optString("ret_msg");

						// 成功
						if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
							// TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
							// if (pay_type_flag == 1) {
							// TextView tv_agree_no = (TextView)
							// findViewById(R.id.tv_agree_no);
							// tv_agree_no.setVisibility(View.VISIBLE);
							// tv_agree_no.setText(objContent.optString(
							// "agreementno", ""));
							// }
							BaseHelper.showDialog(RechargeActivity.this, "提示", "支付成功",
									android.R.drawable.ic_dialog_alert);

//							NofifyUserinfoChanged nofifyUserinfoChanged = new NofifyUserinfoChanged() {
//
//								@Override
//								public void onNotifyUserinfoChange() {
//									CommonUtil.setmNofifyUserinfoChanged(null);
//									startActivity(new Intent(RechargeActivity.this, HomeActivity.class)
//											.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("page", 2));  // 跳转财富
//									RechargeActivity.this.finish();
//								}
//							};
//							CommonUtil.setmNofifyUserinfoChanged(nofifyUserinfoChanged);
//							CommonUtil.getUserInfo(LicaiApplication.getId(), LicaiApplication.getToken(RechargeActivity.this), RechargeActivity.this, false);

						} else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
							// 处理中，掉单的情形
							String resulPay = objContent.optString("result_pay");
							if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay)) {
								BaseHelper.showDialog(RechargeActivity.this, "提示",
										objContent.optString("ret_msg"),
										android.R.drawable.ic_dialog_alert);
							}

						} else {
							// 失败
							BaseHelper.showDialog(RechargeActivity.this, "提示", retMsg,
									android.R.drawable.ic_dialog_alert);
						}
					}
					break;
				}
				super.handleMessage(msg);
			}
		};
	}

}
