package com.example.recorder;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RecorderAdapter extends ArrayAdapter<MainActivity.Recorder> {

    private int mMinItemWidth;
    private int mMaxItemWidth;

    private List<MainActivity.Recorder> mDatas;
    private Context mContext;

    public RecorderAdapter(Context context, List<MainActivity.Recorder> mDatas) {
        super(context, -1, mDatas);
        this.mContext = context;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWidth = (int) (outMetrics.widthPixels * 0.3f);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_recorder, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.seconds = (TextView) view.findViewById(R.id.id_recorder_time);
            viewHolder.mLength = (View) view.findViewById(R.id.id_recorder_length);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.seconds.setText(Math.round(getItem(position).getTime()) + "\"");
        ViewGroup.LayoutParams lp = viewHolder.mLength.getLayoutParams();
        lp.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f * getItem(position).getTime()));
        return view;
    }

    class ViewHolder {
        TextView seconds;
        View mLength;
    }
}
