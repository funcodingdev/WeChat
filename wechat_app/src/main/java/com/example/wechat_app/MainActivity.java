package com.example.wechat_app;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wechat_app.fragment.TabFragment;
import com.example.wechat_app.utils.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity"+":";
    private ViewPager mVpMain;
    private List<String> mTitles = new ArrayList<>(Arrays.asList("微信", "通讯录", "发现", "我"));
    private Button mBtnWeChat;
    private Button mBtnFriend;
    private Button mBtnFind;
    private Button mBtnMine;
    private SparseArray<TabFragment> mFragments = new SparseArray<>();
    private List<Button> mTabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initViewPageAdapter();
    }

    private void initViewPageAdapter() {
        mVpMain.setOffscreenPageLimit(mTitles.size());
        mVpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                TabFragment fragment = TabFragment.newInstance(mTitles.get(i));
                fragment.setmListener(new TabFragment.OnTitleClickListener() {
                    @Override
                    public void onClick(String title) {
                        changeWeChatTab(title);
                    }
                });
                return fragment;
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                TabFragment fragment = (TabFragment) super.instantiateItem(container, position);
                mFragments.put(position,fragment);
                return fragment;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                mFragments.remove(position);
                super.destroyItem(container, position, object);
            }
        });
        mVpMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
//                L.d("i="+i+",v="+v+",i1="+i1);
                //左->右 i:0-1 v:0-1
                //left progress:1-0(1-v) right progress:0-1(v)
                //右->左 i:1-0 v:1-0
                if(v > 0){
                    Button left = mTabs.get(i);
                    Button right = mTabs.get(i+1);
                    left.setText((1-v)+"");
                    right.setText(v+"");
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initViews() {
        mVpMain = (ViewPager) findViewById(R.id.vp_main);
        mBtnWeChat = (Button) findViewById(R.id.btn_wechat);
        mBtnFriend = (Button) findViewById(R.id.btn_friend);
        mBtnFind = (Button) findViewById(R.id.btn_find);
        mBtnMine = (Button) findViewById(R.id.btn_mine);
        mTabs.add(mBtnWeChat);
        mTabs.add(mBtnFriend);
        mTabs.add(mBtnFind);
        mTabs.add(mBtnMine);
    }

    public void changeWeChatTab(String title){
        L.d(TAG+"changeWeChatTab="+title);
    }
}
