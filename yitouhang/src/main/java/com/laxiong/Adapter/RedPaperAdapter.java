package com.laxiong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongshidai.mistGSD.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiejin on 2016/4/14.
 * Types RedPaperAdapter.java
 */
public class RedPaperAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<RedPaper> list;

    public RedPaperAdapter(Context context, ArrayList<RedPaper> list) {
        this.mContext = context;
        this.list = list;
    }

    public void setList(ArrayList<RedPaper> list) {
        this.list = list;
        if (list.size() > 0)
            this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list.size() > 0)
            return list.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_redpaper, null);
            viewholder = new ViewHolder();
            viewholder.tvname = (TextView) convertView.findViewById(R.id.tv_name);
            viewholder.tvexpire = (TextView) convertView.findViewById(R.id.tv_expire);
            viewholder.tvexplain = (TextView) convertView.findViewById(R.id.tv_content);
            viewholder.tvyuan = (TextView) convertView.findViewById(R.id.tv_yuan);
            viewholder.tv = (TextView) convertView.findViewById(R.id.tv);
            viewholder.rlbg = (RelativeLayout) convertView.findViewById(R.id.rl_bg);
            viewholder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        RedPaper paper = list.get(position);
        if (paper.getIs_used() == RedPaper.UsetypeEnum.UNUSED.getVal()) {
            viewholder.tvname.setTextColor(mContext.getResources().getColor(R.color.yellow_paper));
            viewholder.tvexpire.setTextColor(mContext.getResources().getColor(R.color.yellow_paper));
            viewholder.tvexplain.setTextColor(mContext.getResources().getColor(R.color.grey_bg_text));
            viewholder.tvyuan.setTextColor(mContext.getResources().getColor(R.color.yellow_paper));
            viewholder.tvyuan.setTextColor(mContext.getResources().getColor(R.color.yellow_paper));
            viewholder.tv.setTextColor(mContext.getResources().getColor(R.color.yellow_paper));
            viewholder.rlbg.setBackgroundResource(R.drawable.hongbao);//设置黄色背景
        } else {
            viewholder.tvname.setTextColor(mContext.getResources().getColor(R.color.grey_bg_text));
            viewholder.tvexpire.setTextColor(mContext.getResources().getColor(R.color.grey_bg_text));
            viewholder.tvexplain.setTextColor(mContext.getResources().getColor(R.color.grey_bg_text));
            viewholder.tvyuan.setTextColor(mContext.getResources().getColor(R.color.grey_bg_text));
            viewholder.tvyuan.setTextColor(mContext.getResources().getColor(R.color.grey_bg_text));
            viewholder.tv.setTextColor(mContext.getResources().getColor(R.color.grey_bg_text));
            viewholder.rlbg.setBackgroundResource(R.drawable.hongbao_gray);//设置黄色背景
        }
        viewholder.tvname.setText(paper.getRedtype());
        viewholder.tvexpire.setText("有效期至" + paper.getDate());
        viewholder.tvexplain.setText(paper.getRule());
        viewholder.tvyuan.setText(paper.getAmount() + "");
        viewholder.iv_select.setImageResource(paper.isSelected() ? R.drawable.choose : 0);
        return convertView;
    }

    class ViewHolder {
        RelativeLayout rlbg;
        TextView tvname;
        TextView tvexpire;
        TextView tvexplain;
        TextView tvyuan;
        TextView tv;
        ImageView iv_select;
    }
}
