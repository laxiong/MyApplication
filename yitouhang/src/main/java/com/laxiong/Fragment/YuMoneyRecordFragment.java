package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.laxiong.yitouhang.R;

/**
 * Created by admin on 2016/4/6.
 */
@SuppressLint("NewApi")
public class YuMoneyRecordFragment extends Fragment {
    /***
     * 余额记录的碎片
     */
    private View mView ;
    private ListView mListView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView =inflater.inflate(R.layout.withdraw_listview,null);
        initView();
        return mView;
    }

    private void initView(){
        mListView = (ListView)mView.findViewById(R.id.listview);
        mListView.setAdapter(adapter);
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder mhonder ;
            if(view==null){
                mhonder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.investmentrecord_buying_item,null);
                mhonder.mProject = (TextView)view.findViewById(R.id.name);
                mhonder.mTime = (TextView) view.findViewById(R.id.time);
                mhonder.mRechargeMoney = (TextView)view.findViewById(R.id.money_type);
                mhonder.mNotif = (TextView)view.findViewById(R.id.notif_msg);
                view.setTag(mhonder);
            }else{
                mhonder = (ViewHolder)view.getTag();
            }
            mhonder.mProject.setText("一度人脉收益");
            mhonder.mTime.setText("2016-03-15");
            mhonder.mRechargeMoney.setText("+23元");
            mhonder.mRechargeMoney.setTextColor(Color.parseColor("#FFEE7737"));
            mhonder.mNotif.setText("已回款至账户余额");
            if(i ==2){
                mhonder.mProject.setText("碳银宝3期冻结收益");
                mhonder.mTime.setText("2016-03-16");
                mhonder.mRechargeMoney.setText("＋23.22元");
                mhonder.mRechargeMoney.setTextColor(Color.parseColor("#FFEE7737"));
                mhonder.mNotif.setText("到期自动赎回至账户余额");
            }

            if(i ==3){
                mhonder.mProject.setText("购买碳银宝6期");
                mhonder.mTime.setText("2016-03-17");
                mhonder.mRechargeMoney.setText("-8,000元");
                mhonder.mRechargeMoney.setTextColor(Color.parseColor("#FF00C021"));
                mhonder.mNotif.setText("");
            }
            if(i ==4){
                mhonder.mProject.setText("二度人脉收益");
                mhonder.mTime.setText("2016-03-18");
                mhonder.mRechargeMoney.setText("8.26元");
                mhonder.mRechargeMoney.setTextColor(Color.parseColor("#FFEE7737"));
                mhonder.mNotif.setText("已回款至账户余额");
            }
            if(i ==6){
                mhonder.mProject.setText("抵扣红包");
                mhonder.mTime.setText("2016-03-20");
                mhonder.mRechargeMoney.setText("+8元");
                mhonder.mRechargeMoney.setTextColor(Color.parseColor("#FFEE7737"));
                mhonder.mNotif.setText("已回款至账户余额");
            }

            return view;
        }
    };

    class ViewHolder{
        TextView mProject ;
        TextView mTime ;
        TextView mRechargeMoney ;
        TextView mNotif ;
    }



}
