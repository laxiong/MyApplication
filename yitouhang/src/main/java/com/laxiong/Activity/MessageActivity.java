package com.laxiong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.laxiong.Adapter.ReuseAdapter;
import com.laxiong.Adapter.ViewHolder;
import com.laxiong.Mvp_model.Message;
import com.laxiong.Mvp_presenter.Message_Presenter;
import com.laxiong.Mvp_view.IViewMessage;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.yitouhang.R;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends BaseActivity implements OnClickListener, IViewMessage {
    /***
     * 消息
     */
    private FrameLayout mBack;
    private TextView mTitle;
    private ListView lvlist;
    private ReuseAdapter<Message> adapter;
    private Message_Presenter presenter;
    private List<Message> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_layout);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        lvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id != -1) {
                    Message msg = list.get(position);
                    Intent intent = new Intent(MessageActivity.this, WebViewActivity.class);
                    intent.putExtra("url", msg.getUrl());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void loadMsgSuc(List<Message> listdata) {
        if (listdata != null && listdata.size() > 0) {
            this.list.addAll(listdata);
            adapter.setList(list);
        }
    }

    @Override
    public void loadMsgFailure(String msg) {
        ToastUtil.customAlert(this, msg);
    }

    private void initData() {
        presenter = new Message_Presenter(this);
        mBack.setOnClickListener(this);
        list = new ArrayList<Message>();
        adapter = new ReuseAdapter<Message>(this, list, R.layout.message_listview_item) {
            @Override
            public void convert(ViewHolder helper, Message item) {
                helper.setText(R.id.tv_type, item.getType_id() == 2 ? "公告" : "");
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_time, item.getAdd_time());
                TextView tv_type = helper.getView(R.id.tv_type);
                tv_type.setBackgroundColor(getResources().getColor(item.is_active() ? R.color.unread_msg : R.color.readed_msg));
            }
        };
        lvlist.setAdapter(adapter);
        presenter.loadMsgList(this);
    }

    private void initView() {
        mBack = (FrameLayout) findViewById(R.id.back_layout);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("消息");
        lvlist = (ListView) findViewById(R.id.info_listview);
    }

    @Override
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.back_layout:
                this.finish();
                break;
        }
    }
}
