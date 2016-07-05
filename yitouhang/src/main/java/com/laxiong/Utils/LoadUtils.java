package com.laxiong.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carfriend.mistCF.R;

/**
 * Created by admin on 2016/6/23.
 */
public class LoadUtils {

    // 提示加载中的进度条

    public static Dialog createbuildDialog(Context mContext,String msg){

        View mView = LayoutInflater.from(mContext).inflate(R.layout.loading_dialog,null);
        ImageView mImg = (ImageView)mView.findViewById(R.id.img);
        TextView mText = (TextView)mView.findViewById(R.id.text);
        LinearLayout dialog_view = (LinearLayout)mView.findViewById(R.id.dialog_view);

        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);
        mImg.startAnimation(anim);
//        mText.setTextColor(Color.WHITE);
        mText.setText(msg);

        Dialog dialog = new Dialog(mContext,R.style.loading_dialog);

        dialog.setContentView(dialog_view,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return  dialog ;
    }

}
