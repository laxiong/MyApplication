package com.laxiong.Mvp_presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_view.IViewChangePwd;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Mvp_view.IViewReBackPwd;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.UserLogin;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiejin on 2016/4/19.
 * Types Password_Presenter.java
 */
public class Password_Presenter {
    private IViewChangePwd iviewchange;
    private IViewReBackPwd iviewback;
    private IViewCommonBack iviewresetpay;
    private long startmiles = -1;
    private static final long INTER_TIME = 30000;

    public Password_Presenter(IViewChangePwd iviewchange) {
        this.iviewchange = iviewchange;
    }

    public Password_Presenter(IViewReBackPwd iviewback) {
        this.iviewback = iviewback;
    }

    public Password_Presenter(IViewCommonBack iviewreset) {
        this.iviewresetpay = iviewreset;
    }

    public void reqResetPayPwd(Context context, String name, String vali, String identi, String newpwd) {
        String authori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(authori) || StringUtils.testBlankAll(name, vali, identi, newpwd))
            return;
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.RPAY.getVal());
        params.put("realname", name);
        params.put("idc", identi);
        params.put("code", vali);
        params.put("pay_pwd", newpwd);
        HttpUtil.put(InterfaceInfo.USER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewresetpay.reqbackSuc();
                        } else {
                            iviewresetpay.reqbackFail(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewresetpay.reqbackFail(e.toString());
                    }
                } else {
                    iviewresetpay.reqbackFail("出错,无响应");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewresetpay.reqbackFail(responseString);
            }
        }, authori);
    }

    public void reqValidation() {
        if (startmiles == -1) {
            startmiles = System.currentTimeMillis();
        } else {
            long inteval = System.currentTimeMillis() - startmiles;
            if (inteval <= INTER_TIME) {
                iviewback.showTimeOut((int) (inteval / 1000) + 1);
                return;
            } else
                startmiles = System.currentTimeMillis();
        }
        String phonenum = iviewback.getTextPhone();
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.PWD.getVal());
        params.put("phone", phonenum);
        HttpUtil.post(InterfaceInfo.CODE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewback.getCodeSuccess();
                        } else {
                            iviewback.getCodeFailure(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewback.getCodeFailure(e.toString());
                    }
                } else {
                    iviewback.getCodeFailure("出错");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewback.getCodeFailure(responseString);
            }
        }, true);

    }

    public void reqPayCode(Context context) {
        String autori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(autori))
            return;
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.RPAY.getVal());
        HttpUtil.post(InterfaceInfo.CODE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewresetpay.reqbackSuc();
                        } else {
                            iviewresetpay.reqbackFail(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewresetpay.reqbackFail(e.toString());
                    }
                } else {
                    iviewresetpay.reqbackFail("错误,无响应");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewresetpay.reqbackFail(responseString);
            }
        }, autori);
    }

    public void reqChangePayPwd(Context context, String pwd, String newpwd, String code) {
        String autori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(autori))
            return;
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.CPWD.getVal());
        params.put("old_pay_pwd", pwd);
        params.put("pay_pwd", newpwd);
        params.put("repay_pwd", newpwd);
        params.put("code", code);
        HttpUtil.put(InterfaceInfo.USER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewresetpay.reqbackSuc();
                        } else {
                            iviewresetpay.reqbackFail(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewresetpay.reqbackFail(e.toString());
                    }
                } else {
                    iviewresetpay.reqbackFail("无响应");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewresetpay.reqbackFail(responseString);
            }
        }, autori);

    }

    public void reqReBackPwd(Context context) {
        String phonenum = iviewback.getTextPhone();
        String pwd = iviewback.getTextPwd();
        String code = iviewback.getValiCode();
        RequestParams params = new RequestParams();
        params.put("type", Constants.ReqEnum.PWD.getVal());
        params.put("phone", phonenum);
        params.put("pwd", pwd);
        params.put("code", code);
        HttpUtil.put(InterfaceInfo.USER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewback.reBackSuccess();
                        } else {
                            iviewback.reBackFailure(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewback.reBackFailure(e.toString());
                    }
                } else {
                    iviewback.reBackFailure("出错");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String
                    responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewback.reBackFailure(responseString);
            }
        }

                , true);
    }

    public void reqChangePwd(Context context) {
        if (iviewchange == null)
            return;
        String oldpwd = iviewchange.getOldPwd();
        String newpwd = iviewchange.getNewPwd();
        RequestParams params = new RequestParams();
        params.put("type", "edpwd");
        params.put("old_pwd", oldpwd);
        params.put("pwd", newpwd);
        params.put("repwd", newpwd);
        UserLogin userlogin = YiTouApplication.getInstance().getUserLogin();
        if (userlogin == null || StringUtils.isBlank(userlogin.getToken_id() + "") || StringUtils.isBlank(userlogin.getToken())) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
        int tokenid = userlogin.getToken_id();
        String token = userlogin.getToken();
        String str = tokenid + ":" + token;
        String authori = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        HttpUtil.put(InterfaceInfo.USER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response != null) {
                        if (response.getInt("code") == 0)
                            iviewchange.updateSuc();
                        else
                            iviewchange.updateFailure(response.getString("msg"));
                    } else {
                        iviewchange.updateFailure(response.getString("出错"));
                    }
                } catch (JSONException e) {
                    iviewchange.updateFailure(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewchange.updateFailure(responseString);
            }
        }, authori);
    }
}
