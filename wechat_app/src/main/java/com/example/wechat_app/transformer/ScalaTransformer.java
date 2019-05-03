package com.example.wechat_app.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.wechat_app.utils.L;

public class ScalaTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(@NonNull View view, float position) {
//        L.d(view.getTag()+",pos="+position);
        //a->b
        // a:position (0,-1)
        // b:position (1,0)
        //b->a
        //b:position (0,1)
        //a:position (-1,0)
        //(,-1)
        if (position < -1) {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
            view.setAlpha(MIN_ALPHA);
            //(-1,1)
        } else if (position <= 1) {
            if (position < 0) {//左边页面a
                //a->b
                // a:position (0,-1)
                //期望值(1,0.75f)
                float scaleA = MIN_SCALE + (1 - MIN_SCALE) * (1 + position);
                view.setScaleX(scaleA);
                view.setScaleY(scaleA);
                //(1,0.5)
                float alphaA = MIN_ALPHA + (1 - MIN_ALPHA) * (1 + position);
                view.setAlpha(alphaA);
                //b->a
                //a:position (-1,0)
//                MIN_SCALE + (1-MIN_SCALE) * (1+position);
            } else {// 右边页面b
                //a->b
                // b:position (1,0)
                //(0.75f,1)
                float scaleB = MIN_SCALE + (1 - MIN_SCALE) * (1 - position);
                view.setScaleX(scaleB);
                view.setScaleY(scaleB);
                float alphaB = MIN_ALPHA + (1 - MIN_ALPHA) * (1 - position);
                view.setAlpha(alphaB);
                //b->a
                //b:position (0,1)
                //(1,0.75f)
//                MIN_SCALE+(1-MIN_SCALE)*(1-position);
            }

            //(1,)
        } else {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
            view.setAlpha(MIN_ALPHA);
        }
    }
}
