package com.laxiong.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laxiong.Common.Settings;
import com.laxiong.View.PayPop;
import com.laxiong.entity.ShareInfo;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.gongshidai.mistGSD.R;
/**
 * Created by xiejin on 2016/4/7.
 * Types DialogUtils.java
 */
public class DialogUtils {
    private static DialogUtils dutil;
    private PayPop dialog;
    private static Activity context;

    private DialogUtils(Activity context) {
        this.context = context;
    }

    public static DialogUtils getInstance(Activity contexts) {
        if (dutil == null || contexts != context)
            dutil = new DialogUtils(contexts);
        return dutil;
    }

    public void alertShareDialog(final ShareInfo shareInfo, View parent) {
        DialogUtils.bgalpha(context, 0.3f);
        if (dialog != null) {
            DialogUtils.bgalpha(context, 0.3f);
            dialog.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            return;
        }
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_share, null);
        ImageView iv_wx = (ImageView) v.findViewById(R.id.iv_wx);
        ImageView iv_wxcircle = (ImageView) v.findViewById(R.id.iv_wxcircle);
        TextView tv_cancel = (TextView) v.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.bgalpha(context, 1.0f);
                dialog.dismiss();
            }
        });
        iv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int x=shareInfo.getUrl().lastIndexOf("?");
//                String url=shareInfo.getUrl().substring(0,x==-1?shareInfo.getUrl().length()-1:x);
                String eventid="s_"+shareInfo.getTitle();
                MobclickAgent.onEvent(context,eventid);
                new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withMedia(new UMImage(context,shareInfo.getImg()))
                                .withText(shareInfo.getContent())
                                .withTargetUrl(shareInfo.getUrl())
                                .withTitle(shareInfo.getTitle())
                                .share();
                DialogUtils.bgalpha(context, 1.0f);
                dialog.dismiss();
            }
        });
        iv_wxcircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x=shareInfo.getUrl().lastIndexOf("?");
                String url=shareInfo.getUrl().substring(0,x==-1?shareInfo.getUrl().length()-1:x);
                MobclickAgent.onEvent(context,url);
                new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withMedia(new UMImage(context, shareInfo.getImg()))
                        .withText(shareInfo.getContent())
                        .withTargetUrl(shareInfo.getUrl())
                        .withTitle(shareInfo.getTitle())
                        .share();
                DialogUtils.bgalpha(context, 1.0f);
                dialog.dismiss();
            }
        });
        dialog = new PayPop(v, context);
        dialog.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    public static void bgalpha(Activity context, float alpha) {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = alpha;
        context.getWindow().setAttributes(params);
    }

}
