package com.dimon.ganwumei.ui.newsfeed.activity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.injector.HasComponent;
import com.dimon.ganwumei.injector.components.ActivityComponent;
import com.dimon.ganwumei.injector.components.DaggerActivityComponent;
import com.dimon.ganwumei.ui.base.BaseActivity;
import com.dimon.ganwumei.ui.newsfeed.adapter.GanDailyPagerAdapter;
import com.dimon.ganwumei.util.DateUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 干货每日详情Activity
 * Created by Dimon on 2016/3/23.
 */
public class GanDailyActivity extends BaseActivity implements ViewPager.OnPageChangeListener, HasComponent<ActivityComponent> {

    public static final String EXTRA_GAN_DATE = "gan_date";
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBar;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    @Bind(R.id.rootLayout)
    CoordinatorLayout mRootLayout;

    private ActivityComponent activityComponent;
    GanDailyPagerAdapter mPagerAdapter;
    Date mGankDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        ButterKnife.bind(this);
        initializeInjector();
        initToolbar();
        initViewPager();
        initTabLayout();
    }

    private void initializeInjector() {
        this.activityComponent = DaggerActivityComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    private void initToolbar() {
        if (mToolbar == null || mAppBar == null) {
            throw new IllegalStateException("No toolbar");
        }
        mToolbar.setOnClickListener(v -> onToolbarClick());
        setSupportActionBar(mToolbar);
        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            mAppBar.setElevation(10.6f);
        }
        mGankDate = (Date) getIntent().getSerializableExtra(EXTRA_GAN_DATE);
        setTitle(DateUtils.toDate(mGankDate));
    }

    public boolean canBack() {
        return true;
    }

    public void onToolbarClick() {

    }

    private void initViewPager() {
        mPagerAdapter = new GanDailyPagerAdapter(getSupportFragmentManager(), mGankDate);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initTabLayout() {
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE) {
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitle(DateUtils.toDate(mGankDate, -position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mViewPager.removeOnPageChangeListener(this);
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public ActivityComponent getComponent() {
        return activityComponent;
    }
}