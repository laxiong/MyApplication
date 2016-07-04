package com.laxiong.Mvp_presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.Constants;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.Model_Basic2;
import com.laxiong.Mvp_view.IViewChangePwd;
import com.laxiong.Mvp_view.IViewCommonBack;
import com.laxiong.Mvp_view.IViewReBackPwd;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.CommonUtils;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.StringUtils;
import com.laxiong.entity.UserLogin;
import com.loopj.android.http.Base64;
import com.loopj.android.network.JsonHttpResponseHandler;
import com.loopj.android.network.RequestParams;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by xiejin on 2016/4/19.
 * Types Password_Presenter.java
 */
public class Password_Presenter {
    private IViewChangePwd iviewchange;
    private IViewReBackPwd iviewback;
    private IViewCommonBack iviewresetpay;

    public Password_Presenter(IViewChangePwd iviewchange) {
        this.iviewchange = iviewchange;
    }

    public Password_Presenter(IViewReBackPwd iviewback) {
        this.iviewback = iviewback;
    }

    public Password_Presenter(IViewCommonBack iviewreset) {
        this.iviewresetpay = iviewreset;
    }
    public void valifyCode(final Context context,String code){
        String auth=CommonReq.getAuthori(context);
        if(TextUtils.isEmpty(code)||TextUtils.isEmpty(auth))return;
        FormEncodingBuilder builder=new FormEncodingBuilder();
        builder.add("code", code);
        HttpUtil2.post(InterfaceInfo.JGCODE_URL, builder, new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewresetpay.reqbackSuc("vali");
                        } else if (response.getInt("code") == 401) {
                            CommonReq.showReLoginDialog(context);
                        } else {
                            iviewresetpay.reqbackFail(response.getString("msg"), "");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewresetpay.reqbackFail(e.toString(), "");
                    }
                } else {
                    iviewresetpay.reqbackFail("无响应", "");
                }
            }

            @Override
            public void onFailure(String msg) {
                iviewresetpay.reqbackFail(msg, "");
            }
        },auth);
    }
    public void valifyIdenti(final Context context, String realname, String idc) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("idc", idc);
        builder.add("realname", realname);
        HttpUtil2.post(InterfaceInfo.JGIDC_URL, builder, new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewresetpay.reqbackSuc("");
                        } else if (response.getInt("code") == 401) {
                            CommonReq.showReLoginDialog(context);
                        } else {
                            iviewresetpay.reqbackFail(response.getString("msg"), "");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewresetpay.reqbackFail(e.toString(), "");
                    }
                } else {
                    iviewresetpay.reqbackFail("无响应", "");
                }
            }

            @Override
            public void onFailure(String msg) {
                iviewresetpay.reqbackFail(msg, "");
            }
        }, CommonReq.getAuthori(context));
    }

    public void reqResetPayPwd(final Context context, String name, String vali, String identi, String newpwd) {
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
                            iviewresetpay.reqbackSuc(null);
                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(context);
                            } else
                                iviewresetpay.reqbackFail(response.getString("msg"), null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewresetpay.reqbackFail(e.toString(), null);
                    }
                } else {
                    iviewresetpay.reqbackFail("出错,无响应", null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewresetpay.reqbackFail(responseString, null);
            }
        }, authori);
    }
    public void valifyPayPwd(final Context context,final String pwd){
        String author=CommonReq.getAuthori(context);
        if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(author))return;
        FormEncodingBuilder builder=new FormEncodingBuilder();
        builder.add("pay_pwd",pwd);
        builder.add("type", "deal");
        HttpUtil2.post(InterfaceInfo.verifyPay_URL, builder, new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                if(response!=null){
                    try {
                        if(response.getInt("code")==0){
                            iviewresetpay.reqbackSuc(pwd);
                        }else if(response.getInt("code")==401){
                            CommonReq.showReLoginDialog(context);
                        }else{
                            iviewresetpay.reqbackFail(response.getString("msg"),"");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewresetpay.reqbackFail("有一场","");
                    }
                }else{
                    iviewresetpay.reqbackFail("无响应","");
                }
        }

            @Override
            public void onFailure(String msg) {
                iviewresetpay.reqbackFail(msg,"");
            }
        },author);
    }
    public void reqValidation(final Context context) {
        String phonenum = iviewback.getTextPhone();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", Constants.ReqEnum.PWD.getVal());
        builder.add("phone", phonenum);
        HttpUtil2.post(InterfaceInfo.CODE_URL, builder, new Callback() {
            @Override
            public void onFailure(String msg) {
                iviewback.getCodeFailure(msg);
            }

            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewback.getCodeSuccess();
                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(context);
                            } else {//405...
                                iviewback.getCodeFailure("code:" + response.getInt("code") + "msg:" + response.getString("msg"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewback.getCodeFailure("jsonexception" + e.toString());
                    }
                } else {
                    iviewback.getCodeFailure("出错");
                }
            }
        });
    }

    public void reqPayCode(final Context context) {
        String autori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(autori))
            return;
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", Constants.ReqEnum.RPAY.getVal());
        HttpUtil2.post(InterfaceInfo.CODE_URL, builder, new Callback() {
            @Override
            public void onFailure(String msg) {
                iviewresetpay.reqbackFail(msg, null);
            }

            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewresetpay.reqbackSuc(null);
                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(context);
                            } else
                                iviewresetpay.reqbackFail(response.getString("msg"), null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewresetpay.reqbackFail(e.toString(), null);
                    }
                } else {
                    iviewresetpay.reqbackFail("错误,无响应", null);
                }
            }
        },autori);
    }

    public void reqChangePayPwd(final Context context, String pwd, String newpwd, String code) {
        String autori = CommonReq.getAuthori(context);
        if (StringUtils.isBlank(autori))
            return;
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", Constants.ReqEnum.CPWD.getVal());
        builder.add("old_pay_pwd", pwd);
        builder.add("pay_pwd", newpwd);
        builder.add("repay_pwd", newpwd);
        builder.add("code", code);
        HttpUtil2.put(InterfaceInfo.USER_URL, builder, new Callback() {
            @Override
            public void onFailure(String msg) {
                iviewresetpay.reqbackFail(msg, null);
            }

            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewresetpay.reqbackSuc(null);
                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(context);
                            } else
                                iviewresetpay.reqbackFail(response.getString("msg"), null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewresetpay.reqbackFail(e.toString(), null);
                    }
                } else {
                    iviewresetpay.reqbackFail("无响应", null);
                }
            }
        }, autori);
    }

    public void reqReBackPwd(final Context context) {
        String phonenum = iviewback.getTextPhone();
        String pwd = iviewback.getTextPwd();
        String code = iviewback.getValiCode();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", Constants.ReqEnum.PWD.getVal());
        builder.add("phone", phonenum);
        builder.add("pwd", pwd);
        builder.add("code", code);
        HttpUtil2.put(InterfaceInfo.USER_URL, builder, new Callback() {
            @Override
            public void onFailure(String msg) {
                iviewback.reBackFailure(msg);
            }

            @Override
            public void onResponse2(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            iviewback.reBackSuccess();
                        } else {
                            if (response.getInt("code") == 401) {
                                CommonReq.showReLoginDialog(context);
                            } else {
                                iviewback.reBackFailure("code:" + response.getInt("code") + "msg:" + response.getString("msg"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        iviewback.reBackFailure(e.toString());
                    }
                } else {
                    iviewback.reBackFailure("出错");
                }
            }
        });
//        RequestParams params = new RequestParams();
//        params.put("type", Constants.ReqEnum.PWD.getVal());
//        params.put("phone", phonenum);
//        params.put("pwd", pwd);
//        params.put("code", code);
//        HttpUtil.put(InterfaceInfo.USER_URL, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                if (response != null) {
//                    try {
//                        if (response.getInt("code") == 0) {
//                            iviewback.reBackSuccess();
//                        } else {
//                            if (response.getInt("code") == 401)
//                                CommonReq.showReLoginDialog(context);
//                            else
//                                iviewback.reBackFailure(response.getString("msg"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        iviewback.reBackFailure(e.toString());
//                    }
//                } else {
//                    iviewback.reBackFailure("出错");
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String
//                    responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                iviewback.reBackFailure(responseString);
//            }
//        }
//
//                , true);
    }

    public void reqChangePwd(final Context context) {
        if (iviewchange == null)
            return;
        String oldpwd = iviewchange.getOldPwd();
        String newpwd = iviewchange.getNewPwd();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", "edpwd");
        builder.add("old_pwd", oldpwd);
        builder.add("pwd", newpwd);
        builder.add("repwd", newpwd);
        UserLogin userlogin = YiTouApplication.getInstance().getUserLogin();
        if (userlogin == null || StringUtils.isBlank(userlogin.getToken_id() + "") || StringUtils.isBlank(userlogin.getToken())) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
        int tokenid = userlogin.getToken_id();
        String token = userlogin.getToken();
        String str = tokenid + ":" + token;
        String authori = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        HttpUtil2.put(InterfaceInfo.USER_URL, builder, new Callback() {
            @Override
            public void onFailure(String msg) {
                iviewchange.updateFailure(msg);
            }

            @Override
            public void onResponse2(JSONObject response) {
                try {
                    if (response != null) {
                        if (response.getInt("code") == 0)
                            iviewchange.updateSuc();
                        else {
                            if (response.getInt("code") == 401)
                                CommonReq.showReLoginDialog(context);
                            else
                                iviewchange.updateFailure(response.getString("msg"));
                        }
                    } else {
                        iviewchange.updateFailure(response.getString("出错"));
                    }
                } catch (JSONException e) {
                    iviewchange.updateFailure(e.toString());
                    e.printStackTrace();
                }
            }
        }, authori);
    }
}
