package com.example.wechat_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.example.wechat_app.fragment.TabFragment;
import com.example.wechat_app.utils.L;
import com.example.wechat_app.view.TabView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityWithTab extends AppCompatActivity {
    private static final String TAG = "MainActivity"+":";
    private static final String BUNDLE_KEY_POS = "key_pos";
    private ViewPager mVpMain;
    private List<String> mTitles = new ArrayList<>(Arrays.asList("微信", "通讯录", "发现", "我"));
    private TabView mBtnWeChat;
    private TabView mBtnFriend;
    private TabView mBtnFind;
    private TabView mBtnMine;
    private SparseArray<TabFragment> mFragments = new SparseArray<>();
    private List<TabView> mTabs = new ArrayList<>();
    private int mCurTabPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        if(savedInstanceState != null){
            mCurTabPos = savedInstanceState.getInt(BUNDLE_KEY_POS,0);
        }
        initViews();
        initViewPageAdapter();
        initEvents();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_KEY_POS,mVpMain.getCurrentItem());
    }

    private void initEvents() {
        for (int i = 0; i < mTabs.size(); i++) {
            final TabView tabView = mTabs.get(i);
            final int finalI = i;
            tabView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVpMain.setCurrentItem(finalI,false);
                    setCurrentTab(finalI);
                }
            });
        }
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
                    TabView left = mTabs.get(i);
                    TabView right = mTabs.get(i+1);
                    left.setProgress(1-v);
                    right.setProgress(v);
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
        mBtnWeChat = (TabView) findViewById(R.id.tab_wechat);
        mBtnWeChat.setIconAndText(R.drawable.wechat,R.drawable.wechat_select,"微信");
        mBtnFriend = (TabView) findViewById(R.id.tab_friend);
        mBtnFriend.setIconAndText(R.drawable.friend,R.drawable.friend_select,"通讯录");
        mBtnFind = (TabView) findViewById(R.id.tab_find);
        mBtnFind.setIconAndText(R.drawable.find,R.drawable.find_select,"发现");
        mBtnMine = (TabView) findViewById(R.id.tab_mine);
        mBtnMine.setIconAndText(R.drawable.mine,R.drawable.mine_select,"我");
        mTabs.add(mBtnWeChat);
        mTabs.add(mBtnFriend);
        mTabs.add(mBtnFind);
        mTabs.add(mBtnMine);
        setCurrentTab(mCurTabPos);
    }

    private void setCurrentTab(int pos){
        for (int i = 0; i < mTabs.size(); i++) {
            TabView tabView = mTabs.get(i);
            if(i == pos){
                tabView.setProgress(1);
            }else{
                tabView.setProgress(0);
            }
        }
    }

    public void changeWeChatTab(String title){
        L.d(TAG+"changeWeChatTab="+title);
    }
}
