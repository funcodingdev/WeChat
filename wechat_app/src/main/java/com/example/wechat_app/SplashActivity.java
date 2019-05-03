package com.example.wechat_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.wechat_app.fragment.SplashFragment;
import com.example.wechat_app.transformer.ScalaTransformer;

public class SplashActivity extends AppCompatActivity {

    private ViewPager mVpMain;
    private int[] mResIds = new int[]{
        R.drawable.ic_img1,
        R.drawable.ic_img2,
        R.drawable.ic_img3,
        R.drawable.ic_img4
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mVpMain = (ViewPager) findViewById(R.id.vp_main);
        mVpMain.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                SplashFragment fragment = SplashFragment.newInstance(mResIds[i]);
                return fragment;
            }

            @Override
            public int getCount() {
                return mResIds.length;
            }
        });
        mVpMain.setPageTransformer(true,new ScalaTransformer());
    }
}
