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
 * Created by admin on 2016/4/7.
 */
@SuppressLint("NewApi")
public class ProfitRecord_TimeXiTongFragment extends Fragment {
    /**
     * 收益记录--时息通
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
            return 6;
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

            mhonder.mProject.setText("时息通");
            mhonder.mTime.setText("2016-03-20");
            mhonder.mRechargeMoney.setText("+55元");
            mhonder.mRechargeMoney.setTextColor(Color.parseColor("#FFEE7737"));
            mhonder.mNotif.setText("已回款至账户余额");
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