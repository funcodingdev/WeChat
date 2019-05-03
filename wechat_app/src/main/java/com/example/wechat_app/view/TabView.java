package com.example.wechat_app.view;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wechat_app.R;

public class TabView extends FrameLayout {
    private ImageView mIvIcon;
    private ImageView mIvIconSelect;
    private TextView mIvIconTitle;

    private static int COLOR_DEFAULT = Color.parseColor("#ff000000");
    private static int COLOR_SELECT = Color.parseColor("#ff45c01a");

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context,R.layout.tab_view,this);
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mIvIconSelect = (ImageView) findViewById(R.id.iv_icon_select);
        mIvIconTitle = (TextView) findViewById(R.id.iv_icon_title);
        setProgress(0);
    }

    public void setIconAndText(int icon,int iconSelect,String title){
        mIvIcon.setImageResource(icon);
        mIvIconSelect.setImageResource(iconSelect);
        mIvIconTitle.setText(title);
    }

    public void setProgress(float progress){
        mIvIcon.setAlpha(1-progress);
        mIvIconSelect.setAlpha(progress);
        mIvIconTitle.setTextColor((int)new ArgbEvaluator().evaluate(progress,COLOR_DEFAULT,COLOR_SELECT));
    }

}
