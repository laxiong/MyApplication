package com.laxiong.Mvp_presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.laxiong.Activity.LoginActivity;
import com.laxiong.Application.YiTouApplication;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Mvp_model.Model_Basic;
import com.laxiong.Mvp_model.OnLoadBcObjListener;
import com.laxiong.Mvp_view.IViewBasicObj;
import com.laxiong.Mvp_view.IViewLogin;
import com.laxiong.Utils.CommonReq;
import com.laxiong.Utils.HttpUtil;
import com.laxiong.Utils.JSONUtils;
import com.laxiong.Utils.SpUtils;
import com.laxiong.Utils.StringUtils;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.entity.User;
import com.laxiong.entity.UserLogin;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by xiejin on 2016/4/18.
 * Types Login_Presenter.java
 */
public class Login_Presenter implements OnLoadBcObjListener<User>{
    private IViewLogin iviewlogin;
    private IViewBasicObj<User> iviewbcobj;
    private Model_Basic<User> mbc;
    public Login_Presenter(IViewLogin iviewlogin) {
        this.iviewlogin = iviewlogin;
    }
    public Login_Presenter(IViewBasicObj<User> iviewbcobj){
        this.iviewbcobj=iviewbcobj;
        mbc=new Model_Basic<User>();
    }
    @Override
    public void onSuccss(User obj) {
        YiTouApplication.getInstance().setUser(obj);
        iviewbcobj.loadObjSuc(obj);
    }
    @Override
    public void onFailure(String msg) {
        iviewbcobj.loadObjFail(msg);
    }

    public void reqUserMsg(Context context){
        UserLogin userlogin = YiTouApplication.getInstance().getUserLogin();
        if (userlogin == null || StringUtils.isBlank(userlogin.getToken_id() + "") || StringUtils.isBlank(userlogin.getToken())) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return;
        }
        int tokenid = userlogin.getToken_id();
        String autho=CommonReq.getAuthori(context);
        if(TextUtils.isEmpty(autho))
            return;
        mbc.setListenerObj(this);
        mbc.aureqByGetObj(InterfaceInfo.GETCOUNT_URL + tokenid,context,null,"",User.class);
    }
    public TextWatcher getTextWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phonnum = iviewlogin.getInputPhoneNum();
                String pwd = iviewlogin.getInputPwd();
                boolean flag=StringUtils.isBlank(phonnum)||StringUtils.isBlank(pwd);
                iviewlogin.updateButton(!flag);
            }
        };
    }
    public void login(final Context context) {
        final String phonnum = iviewlogin.getInputPhoneNum();
        String pwd = iviewlogin.getInputPwd();
        ToastUtil.customAlert(context,"phone:"+phonnum+"pwd:"+pwd);
        RequestParams params = new RequestParams();
        params.put("phone", phonnum);
        params.put("pwd", pwd);
        HttpUtil.post(InterfaceInfo.LOGIN_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        UserLogin userLogin = JSONUtils.parseObject(response.toString(), UserLogin.class);
                        if (userLogin != null && userLogin.getCode() == 0) {
                            SharedPreferences sp = SpUtils.getSp(context);
                            SpUtils.saveStrValue(sp,SpUtils.USERLOGIN_KEY,response.toString());
                            SpUtils.saveStrValue(sp,SpUtils.USER_KEY,phonnum);
                            YiTouApplication.getInstance().setUserLogin(userLogin);
                            iviewlogin.loginsuccess();
                            CommonReq.reqUserMsg(context);
                        } else {
                            iviewlogin.loginfailed(userLogin.getMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        iviewlogin.loginfailed(e.toString());
                    }
                }else{
                    iviewlogin.loginfailed("出错");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                iviewlogin.loginfailed(responseString);
            }
        }, true);
    }
}
