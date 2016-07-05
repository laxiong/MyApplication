package com.laxiong.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carfriend.mistCF.R;
import com.laxiong.Activity.GuXiBaoActivity;
import com.laxiong.Activity.TimeXiTongActivity;
import com.laxiong.Activity.WebViewActivity;
import com.laxiong.Basic.Callback;
import com.laxiong.Common.InterfaceInfo;
import com.laxiong.Json.FinanceJsonBean;
import com.laxiong.Utils.HttpUtil2;
import com.laxiong.Utils.ToastUtil;
import com.laxiong.View.CircleProgressBar;
import com.laxiong.View.FinancingListView;
import com.laxiong.View.PrecentCricleBar;
import com.laxiong.View.WaitPgView;
import com.laxiong.entity.FinanceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class FinancingFragment extends Fragment implements OnClickListener {
    /****
     * 理财的碎片
     */

    private View view;
    private ImageView mConcel_img;
    private LinearLayout mFinancelMessage; // 提示消息

    private FinanceJsonBean mFinanBean = null;
    private FinancingListView mListView;
    private WaitPgView wp;
    private mFinanceAdapter adapter;
    private static final String NO_SALE = "#FFD6D6D6";

    private List<FinanceInfo> mList = new ArrayList<FinanceInfo>();
    ;// 全是固息宝的
    private int listNum;  //
    private TextView mNoticeMsg;
    private UpdateReceiver receiver;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mListView.completeRefresh();
                    break;
                case 2:
                    mListView.completeRefresh();
                    break;
            }
        }
    };

    private void registerUpdateReceiver() {
        receiver = new UpdateReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter("com.action.updatedata"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerUpdateReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.financing_layout, null);
        } else {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
        }
        initView();
        return view;
    }

    private void initView() {
        mConcel_img = (ImageView) view.findViewById(R.id.concel_img);
        mFinancelMessage = (LinearLayout) view.findViewById(R.id.finance_message);
        mNoticeMsg = (TextView) view.findViewById(R.id.notice_msg);
        mConcel_img.setOnClickListener(this);
        mFinancelMessage.setOnClickListener(this);
        mListView = (FinancingListView) view.findViewById(R.id.Listview);
        showLoadView(true);
        getProductInfo();
        getNoticeMsg();
        mListView.setOnRefreshListener(mRefresh);

    }

    class UpdateReceiver extends BroadcastReceiver {
        private FinanceInfo target;

        @Override
        public void onReceive(Context context, Intent intent) {
            target = null;
            int id = intent.getIntExtra("id", -1);
            if (id != -1) {
                FinanceInfo insxt = mFinanBean.getSxt();
                if (id != -1 && insxt.getId() == id) {
                    target = insxt;
                }
                for (FinanceInfo info : mList) {
                    if (info.getId() == id) {
                        target = info;
                        break;
                    }
                }
                if (target != null) {
                    HttpUtil2.get(InterfaceInfo.PRODUCT_URL + "?id=" + id, new Callback() {
                        @Override
                        public void onResponse2(JSONObject response) {
                            try {
                                if (response == null || response.getInt("code") != 0)
                                    return;
                                Double percent = response.getDouble("percent");
                                target.setPercent(percent);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(String msg) {

                        }
                    });
                }
            }
        }
    }

    public void showLoadView(boolean flag) {
        wp = (WaitPgView) view.findViewById(R.id.wp_load);
        wp.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    FinancingListView.OnRefreshListener mRefresh = new FinancingListView.OnRefreshListener() {
        // 刷新
        @Override
        public void onPullRefresh() {
            requestDataFromServer(true);
            pager = 1;
            if (mList != null && mFinanBean != null) {
                mList.removeAll(mFinanBean.getGxb());
                mList.remove(mFinanBean.getSxt());
            }
            getProductInfo();
        }

        //加载更多
        @Override
        public void onLoadingMore() {
            requestDataFromServer(false);
            if (listNum > mList.size() + 1) {
                pager = pager + 1;
                getProductInfo();
            } else {
                Toast.makeText(getActivity(), "全部加载完毕", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void requestDataFromServer(final boolean isLoading) {
        new Thread() {
            public void run() {
                SystemClock.sleep(3000);
                Message msg = Message.obtain();
                if (isLoading) {
                    msg.what = 1;
                } else {
                    msg.what = 2;
                }
                //更新UI
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    private String mNoticeUrl = "";
    private String mTitleStr = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.concel_img:
                if (mFinancelMessage != null)
                    mFinancelMessage.setVisibility(View.GONE);
                break;
            case R.id.finance_message:
                if (!mNoticeUrl.equals("") && !mTitleStr.equals("")) {
                    startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("title", mTitleStr).putExtra("url", mNoticeUrl));
                }
                break;
        }
    }

    //Financing Adapter
    class mFinanceAdapter extends BaseAdapter {
        private int currentType;  //当前item类型
        private static final int TYPE_COUNT = 2;//item类型的总数
        private static final int TYPE_Title = 0;// 标题
        private static final int TYPE_Content = 1;//内容

        @Override
        public int getCount() {
            if (mFinanBean != null) {
                int count = 3 + mList.size();
                return count;
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHonder mViewHonder = null;
            currentType = getItemViewType(i);
            if (view == null) {
                mViewHonder = new ViewHonder();
                mViewHonder.iv = new ImageView(getActivity());
                switch (currentType) {
                    case TYPE_Title:
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.item_section, null);
                        mViewHonder.mText_section = (TextView) view.findViewById(R.id.section);
                        break;

                    case TYPE_Content:
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.item_items, null);
                        mViewHonder.mText_vip = (TextView) view.findViewById(R.id.vip_baifenbi);
                        mViewHonder.mInterge = (TextView) view.findViewById(R.id.tv1);
                        mViewHonder.mPoint = (TextView) view.findViewById(R.id.xiaoshu);
                        mViewHonder.mCircleProgressView = (CircleProgressBar) view.findViewById(R.id.cricleprogress);

                        mViewHonder.profit_tv = (TextView) view.findViewById(R.id.profit_tv);
                        mViewHonder.mProject = (TextView) view.findViewById(R.id.rel_tv1);
                        mViewHonder.showRel = (RelativeLayout) view.findViewById(R.id.rel_tv2);

                        mViewHonder.mNewPerson = (RelativeLayout) view.findViewById(R.id.new_person);
                        mViewHonder.rl_days = (RelativeLayout) view.findViewById(R.id.days);
                        mViewHonder.mLimitDay = (TextView) view.findViewById(R.id.limit_day);
                        mViewHonder.mEnought = (PrecentCricleBar) view.findViewById(R.id.precent_enough);

                        break;
                }
                view.setTag(mViewHonder);
            } else {
                mViewHonder = (ViewHonder) view.getTag();
            }

            if (currentType == TYPE_Title) {
                if (i == 0) {
                    if (mViewHonder.mText_section != null)
                        mViewHonder.mText_section.setText("时息通");
                } else if (i == 2) {
                    if (mViewHonder.mText_section != null)
                        mViewHonder.mText_section.setText("固息宝");
                }
            }
            if (currentType == TYPE_Content) {
                if (i == 1) {  // 时息通
                    final FinanceInfo sxt = mFinanBean.getSxt();
                    if (mViewHonder.mInterge != null && mViewHonder.mPoint != null) {
                        double apr = sxt.getApr();
                        if (isInterge(apr)) {
                            String aprStr = String.valueOf(apr);
                            String[] arr = aprStr.split("[.]");
                            String zhengshu = arr[0];
                            mViewHonder.mInterge.setText(zhengshu);
                            mViewHonder.mPoint.setVisibility(View.GONE);
                        } else {
                            mViewHonder.mPoint.setVisibility(View.VISIBLE);
                            String aprStr = String.valueOf(apr);
                            String[] arr = aprStr.split("[.]");
                            String zhengshu = arr[0];
                            mViewHonder.mInterge.setText(zhengshu);
                            String xiaoshu = arr[1];
                            mViewHonder.mPoint.setText("." + xiaoshu);
                        }
                    }
                    if (mViewHonder.mCircleProgressView != null && mViewHonder.mEnought != null) {
                        mViewHonder.mEnought.setPaintColor(NO_SALE);
                        if (sxt.getPercent() == 100.0) {
                            mViewHonder.mCircleProgressView.setVisibility(View.INVISIBLE);
                            mViewHonder.mCircleProgressView.setPaintColor("#FFEE4E42");
                            mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(sxt.getPercent())),
                                    mViewHonder.iv);
                            mViewHonder.mEnought.setVisibility(View.VISIBLE);
                        } else {
                            mViewHonder.mEnought.setVisibility(View.INVISIBLE);
                            mViewHonder.mCircleProgressView.setVisibility(View.VISIBLE);
                            mViewHonder.mCircleProgressView.setPaintColor("#FFEE4E42");
                            mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(sxt.getPercent())),
                                    mViewHonder.iv);
                        }
                    }

                    if (mViewHonder.mText_vip != null) {
                        if (sxt.getVip() == 0.0) {
                            mViewHonder.mText_vip.setVisibility(View.INVISIBLE);
                        } else {
                            mViewHonder.mText_vip.setVisibility(View.VISIBLE);
                            mViewHonder.mText_vip.setText("+" + String.valueOf(sxt.getVip()) + "%");
                        }
                    }

                    // 日期// 新手标
                    if (mViewHonder.rl_days != null)
                        mViewHonder.rl_days.setVisibility(View.GONE);
                    if (mViewHonder.mNewPerson != null)
                        mViewHonder.mNewPerson.setVisibility(View.GONE);

                    // 标题
                    if (mViewHonder.mProject != null)
                        mViewHonder.mProject.setText(sxt.getTitle());

                    //支付方式
                    if (mViewHonder.profit_tv != null)
                        mViewHonder.profit_tv.setText(sxt.getPaytype());

                    if (view != null)
                        view.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                SharedPreferences sfsxt = getActivity().getSharedPreferences("SXT_ID", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sfsxt.edit();
                                editor.putInt("sxt_id", sxt.getId());
                                editor.commit();

                                getActivity().startActivity(new Intent(getActivity(),
                                        TimeXiTongActivity.class).
                                        putExtra("id", sxt.getId()).
                                        putExtra("isVip", false));
                            }
                        });

                } else if (i >= 3) { // 固息宝
//					List<FinanceInfo> mListGXB = mFinanBean.getGxb();
                    final FinanceInfo gxb = mList.get(i - 3);
                    if(gxb==null)
                        return null;
                    if (mViewHonder.mInterge != null && mViewHonder.mPoint != null) {
                        double apr = gxb.getApr();
                        if (isInterge(apr)) {
                            String aprStr = String.valueOf(apr);
                            String[] arr = aprStr.split("[.]");
                            String zhengshu = arr[0];
                            mViewHonder.mInterge.setText(zhengshu);
                            mViewHonder.mPoint.setVisibility(View.GONE);
                        } else {
                            mViewHonder.mPoint.setVisibility(View.VISIBLE);
                            String aprStr = String.valueOf(apr);
                            String[] arr = aprStr.split("[.]");
                            String zhengshu = arr[0];
                            mViewHonder.mInterge.setText(zhengshu);
                            String xiaoshu = arr[1];
                            mViewHonder.mPoint.setText("." + xiaoshu);
                        }
                    }
                    if (mViewHonder.mCircleProgressView != null && mViewHonder.mEnought != null) {
                        mViewHonder.mEnought.setPaintColor(NO_SALE);
                        if (gxb.getPercent() == 100.0) {
                            mViewHonder.mCircleProgressView.setVisibility(View.INVISIBLE);
                            mViewHonder.mCircleProgressView.setPaintColor("#FFEE4E42");
                            mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(gxb.getPercent())),
                                    mViewHonder.iv);
                            mViewHonder.mEnought.setVisibility(View.VISIBLE);

                        } else {
                            mViewHonder.mEnought.setVisibility(View.INVISIBLE);
                            mViewHonder.mCircleProgressView.setVisibility(View.VISIBLE);
                            mViewHonder.mCircleProgressView.setPaintColor("#FFEE4E42");
                            mViewHonder.mCircleProgressView.setProgress(Float.parseFloat(String.valueOf(gxb.getPercent())),
                                    mViewHonder.iv);
                        }
                    }

                    if (mViewHonder.mText_vip != null) {
                        if (gxb.getVip() == 0.0) {
                            mViewHonder.mText_vip.setVisibility(View.INVISIBLE);
                        } else {
                            mViewHonder.mText_vip.setVisibility(View.VISIBLE);
                            mViewHonder.mText_vip.setText("+" + String.valueOf(gxb.getPresent()) + "%");
                        }
                    }

                    // 新手标
                    final double bird = gxb.getBird();
                    if (mViewHonder.mNewPerson != null) {
                        if (bird == 1) {
                            mViewHonder.mNewPerson.setVisibility(View.VISIBLE);
                        } else {
                            mViewHonder.mNewPerson.setVisibility(View.INVISIBLE);
                        }
                    }
                    //日期
                    final int limit = gxb.getLimit();
                    if (mViewHonder.mLimitDay != null && limit != 0 && mViewHonder.rl_days != null) {
                        mViewHonder.rl_days.setVisibility(View.VISIBLE);
                        mViewHonder.mLimitDay.setText(limit + "天");
                    } else {
                        mViewHonder.rl_days.setVisibility(View.GONE);
                    }

                    //标题
                    if (mViewHonder.mProject != null)
                        mViewHonder.mProject.setText(gxb.getTitle());

                    //支付方式
                    if (mViewHonder.profit_tv != null)
                        mViewHonder.profit_tv.setText(gxb.getPaytype());

                    if (view != null)
                        view.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (gxb.getPercent() != 100.0) {  // 如果是售罄就无法点击进入到详情页面
                                    getActivity().startActivity(new Intent(getActivity(),
                                            GuXiBaoActivity.class).
                                            putExtra("id", gxb.getId()).
                                            putExtra("ttnum", listNum).
                                            putExtra("limitday", limit).
                                            putExtra("isVip", false).
                                            putExtra("bird", bird));
                                }
                            }
                        });
                }
            }
            return view;
        }

        @Override
        public int getItemViewType(final int position) {
            // 时息通
            if (position == 0 || position == 2) {
                return TYPE_Title;
            } else {
                return TYPE_Content;
            }
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_COUNT;
        }
    }

    /**
     * ViewHonder的类
     */
    class ViewHonder {
        TextView mText_section;
        CircleProgressBar mCircleProgressView;//正常的情况下显示的
        PrecentCricleBar mEnought;  //100%时显示的
        TextView mText_vip;
        TextView mInterge; //整位数
        TextView mPoint; // 小数点后的数
        ImageView iv; //放cricleprogressbar的节点
        TextView mProject;
        TextView profit_tv;
        RelativeLayout mNewPerson; //新手标
        RelativeLayout rl_days;
        RelativeLayout showRel;
        TextView mLimitDay; // 日期
    }

    //判断是否为整数
    private boolean isInterge(double num) {
        if (num % 1 == 0) {
            return true;
        } else {
            return false;
        }
    }


    // 获取产品信息
    private int pager = 1;

    private void getProductInfo() {
        HttpUtil2.get(InterfaceInfo.PRODUCT_URL+"?p="+pager+"&limit=10", new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                showLoadView(false);
                if (response != null) {
                    try {
                        if (response.getInt("code") == 0) {
                            // 时息通
                            if (pager == 1) {
                                mFinanBean = new FinanceJsonBean();
                                JSONObject sxt = response.getJSONObject("sxt");
                                FinanceInfo sxtInfo = setProductInfo(sxt);
                                mFinanBean.setSxt(sxtInfo);
                            }
                            // 固息宝
                            JSONArray gxb = response.getJSONArray("gxb");
                            if (gxb.length() > 0) {
                                for (int i = 0; i < gxb.length(); i++) {
                                    JSONObject gxb_obj = gxb.getJSONObject(i);
                                    FinanceInfo gxbInfo = setProductInfo(gxb_obj);
                                    mList.add(gxbInfo);
                                }
                                mFinanBean.setGxb(mList);
                            }
                            mFinanBean.setMsg(response.getString("msg"));
                            mFinanBean.setTime(response.getString("time"));
                            listNum = response.getInt("ttnum");

                            if (pager == 1) {
                                adapter = new mFinanceAdapter();
                                mListView.setAdapter(adapter);
                            } else {
                                mListView.invalidate();
                            }

                        } else {
                            ToastUtil.customAlert(getActivity(),response.getString("msg"));
                        }
                    } catch (Exception e) {
                    }
                }
            }
            @Override
            public void onFailure(String msg) {
                showLoadView(false);
                ToastUtil.customAlert(getActivity(), "网络访问失败");
            }
        });

    }

    // 设置产品的信息
    private FinanceInfo setProductInfo(JSONObject obj) {
        if (obj != null) {
            try {
                FinanceInfo info = new FinanceInfo();
                info.setId(obj.getInt("id"));
                info.setAmount(obj.getInt("amount"));
                info.setApr(obj.getDouble("apr"));
                info.setBird(obj.getInt("bird"));
                info.setBirdapr(obj.getDouble("birdapr"));
                info.setContent(obj.getString("content"));
                info.setDate(obj.getString("date"));
                info.setKey(obj.getString("key"));
                info.setLimit(obj.getInt("limit"));
                info.setMax(obj.getInt("max"));
                info.setTitle(obj.getString("title"));
                info.setType(obj.getInt("type"));
                info.setMembers(obj.getInt("members"));
                info.setPaytype(obj.getString("paytype"));
                info.setPercent(obj.getDouble("percent"));
                info.setRemark(obj.getString("remark"));
                info.setRule(obj.getString("rule"));
                info.setMin(obj.getString("min"));
                info.setScore(obj.getInt("score"));
                info.setPresent(obj.getDouble("present"));
                info.setAccum(obj.getDouble("accum"));
                info.setVip(obj.getDouble("vip"));
                info.setUrl(obj.getString("url"));
                info.setTotal_amount(obj.getInt("total_amount"));
                info.setTotal_menber(obj.getInt("total_menber"));
                return info;
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "FinanceInfo为空", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    // 用于隐藏天数
    private boolean hideTextDay(double day) {
        if (day == 0.0) {
            return true;
        }
        return false;
    }

    // 用于隐藏 新手标
    private boolean hideTextPerson(double strs) {
        if (strs == 1) {
            return false;
        }
        return true;
    }

    //获取理财的置顶的内容
    private void getNoticeMsg() {
        HttpUtil2.get(InterfaceInfo.BASE_URL + "/ggao", new Callback() {
            @Override
            public void onResponse2(JSONObject response) {
                try {
                    if (response != null) {
                        if (response.getInt("code") == 0) {
                            mTitleStr = response.getString("title");
                            mNoticeMsg.setText(mTitleStr);
                            mNoticeUrl = response.getString("url");
                        }
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(String msg) {
            }
        });

    }
}
