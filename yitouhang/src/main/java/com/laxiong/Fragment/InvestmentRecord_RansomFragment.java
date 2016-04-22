package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.laxiong.yitouhang.R;

/**
 * Created by admin on 2016/4/6.
 */
@SuppressLint("NewApi")
public class InvestmentRecord_RansomFragment extends Fragment {
    /***
     *
     */
    private View view ;
    private ListView mListView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.withdraw_listview,null);
        initView();
        return view;
    }

    private void initView(){
        mListView = (ListView)view.findViewById(R.id.listview);
        mListView.setAdapter(adapter);
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 9;
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
            if(view==null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.investmentrecord_buying_item,null);
            }
            return view;
        }
    };


}
