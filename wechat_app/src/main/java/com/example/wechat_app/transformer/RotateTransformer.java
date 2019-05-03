package com.example.wechat_app.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class RotateTransformer implements ViewPager.PageTransformer {
    private static final int MAX_ROTATE = 15;//最大旋转角度

    @Override
    public void transformPage(@NonNull View page, float position) {
        //a->b
        // a:position (0,-1)
        // b:position (1,0)
        //b->a
        //b:position (0,1)
        //a:position (-1,0)
        //(,-1)
        if (position < -1) {
            page.setRotation(-MAX_ROTATE);
            page.setPivotY(page.getHeight());
            page.setPivotX(page.getWidth());
        } else if (position <= 1) {
            if (position < 0) {//左边页面a
                page.setPivotY(page.getHeight());
                //(0.5,1)
                page.setPivotX(0.5f * page.getWidth() + 0.5f * (-position) * page.getWidth());
                //(0,-1) -> (0,max)
                page.setRotation(MAX_ROTATE * position);

            } else {// 右边页面b
                //a->b
                //b position :(1,0)
                page.setPivotY(page.getHeight());
                page.setPivotX(page.getWidth() * 0.5f * (1 - position));
                page.setRotation(MAX_ROTATE * position);
            }
        } else {
            page.setRotation(MAX_ROTATE);
            page.setPivotY(page.getHeight());
            page.setPivotX(0);
        }
    }
}
