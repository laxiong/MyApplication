package com.laxiong.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
import com.laxiong.Activity.TrueNameActivity1;

/**
 * Created by admin on 2016/6/16.
 */
public class OpenAccount {
    /**用户开户的提示框**/
    private static OpenAccount instance ;
    private OpenAccount(){
    }

    public synchronized static OpenAccount getInstance(){
        if (instance==null){
            instance = new OpenAccount();
        }
        return  instance ;
    }

    /***
     * 请前往开户
     */
    private PopupWindow BankMsgWindow ;
    private View NoticeView ;
    private ImageView mNoticeCloseimg ;
    private TextView mNoticeMsgHu ;
    public void goToCreateCountNum(final Context context){
        NoticeView = LayoutInflater.from(context).inflate(R.layout.notice_bankmsg_popwindow, null);
        mNoticeMsgHu = (TextView)NoticeView.findViewById(R.id.goto_hu);
        mNoticeCloseimg = (ImageView)NoticeView.findViewById(R.id.img_close);

        mNoticeMsgHu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,
                        TrueNameActivity1.class));
                dissPopWind();
            }
        });

        mNoticeCloseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissPopWind();
            }
        });

        BankMsgWindow = new PopupWindow(NoticeView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
        BankMsgWindow.setTouchable(true);
        BankMsgWindow.setOutsideTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        BankMsgWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.kefu_bg)); //设置半透明
        BankMsgWindow.showAtLocation(NoticeView, Gravity.BOTTOM, 0, 0);
    }

    private void dissPopWind(){
        if (BankMsgWindow!=null&&BankMsgWindow.isShowing()){
            BankMsgWindow.dismiss();
            BankMsgWindow = null ;
        }
    }

}
