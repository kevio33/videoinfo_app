package com.kevin.videoinfo.Activitys.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.kevin.videoinfo.R;
import com.kevin.videoinfo.adapter.MyPagerAdapter;
import com.kevin.videoinfo.databinding.ActivityHomePageBinding;
import com.kevin.videoinfo.entity.TabEntity;
import com.kevin.videoinfo.fragments.CollectFragment;
import com.kevin.videoinfo.fragments.HomeFragment;
import com.kevin.videoinfo.fragments.MyFragment;

import java.util.ArrayList;
import java.util.Random;

public class HomePageActivity extends AppCompatActivity {

    //commonTabLayout的图标配置
    private String[] mTitles = {"首页", "收藏","我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.home_unselect, R.mipmap.collect_unselect,
            R.mipmap.my_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.home_selected, R.mipmap.collect_selected,
            R.mipmap.my_selected};

    //存放每一页的frag
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private ActivityHomePageBinding activityHomePageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityHomePageBinding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(activityHomePageBinding.getRoot());

        mFragments.add(HomeFragment.newInstance());
        mFragments.add(CollectFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        activityHomePageBinding.tl2.setTabData(mTabEntities);
        activityHomePageBinding.vp2.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),mFragments,mTitles));

        setTabClick();
    }

    //设置tab的点击事件，当点击对应tab时候跳转viewpage
    private void setTabClick(){
        activityHomePageBinding.tl2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                activityHomePageBinding.vp2.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    activityHomePageBinding.tl2.showMsg(0, new Random().nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });
        activityHomePageBinding.vp2.setOffscreenPageLimit(mFragments.size());//防止因frags过多，往回拉的时候导致的闪退问题
        activityHomePageBinding.vp2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                activityHomePageBinding.tl2.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}