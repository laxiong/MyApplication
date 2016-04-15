package com.laxiong.Adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by xiejin on 2016/4/8.
 * Types ReuseAdapter.java
 */
public abstract class ReuseAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> list;
    private int layoutid;

    public ReuseAdapter() {

    }
    public void setList(List<T> list){
        this.list=list;
        if(list.size()>0)
            this.notifyDataSetChanged();
    }
    public ReuseAdapter(Context context, List<T> list, int layoutid) {
        this.context = context;
        this.list = list;
        this.layoutid = layoutid;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = ViewHolder.getHolder(context, layoutid, convertView, parent);
        convert(viewholder, list.get(position));
        return viewholder.getConvertView();
    }

    public abstract void convert(ViewHolder viewholder, T item);

    public static class ViewHolder {
        private static View mconvertView;
        private SparseArray<View> sparseview;

        private ViewHolder(Context context, int layoutid, ViewGroup parent) {
            mconvertView = LayoutInflater.from(context).inflate(layoutid, parent, false);
            mconvertView.setTag(this);
            sparseview = new SparseArray<View>();
        }

        public static ViewHolder getHolder(Context context, int layoutid, View convertView, ViewGroup parent) {
            if (convertView == null)
                return new ViewHolder(context, layoutid, parent);
            return (ViewHolder) mconvertView.getTag();
        }

        public View getConvertView() {
            return mconvertView;
        }

        public <T extends View> T getView(int vid) {
            View view = sparseview.get(vid);
            if (view == null) {
                view = mconvertView.findViewById(vid);
                sparseview.put(vid, view);
            }
            return (T) view;
        }

        public void setTextView(int vid, String content) {
            TextView view = getView(vid);
            view.setText(content);
        }

        public void setImageBitmap(int vid, Bitmap bm) {
            ImageView view = getView(vid);
            view.setImageBitmap(bm);
        }

        public void setImageSource(int vid, int sourceid) {
            ImageView view = getView(vid);
            view.setImageResource(sourceid);
        }
    }
}
