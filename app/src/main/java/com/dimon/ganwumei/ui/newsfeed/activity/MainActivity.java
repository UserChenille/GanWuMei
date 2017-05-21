package com.dimon.ganwumei.ui.newsfeed.activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.api.Constant;
import com.dimon.ganwumei.injector.HasComponent;
import com.dimon.ganwumei.injector.components.ActivityComponent;
import com.dimon.ganwumei.injector.components.DaggerActivityComponent;
import com.dimon.ganwumei.ui.AboutActivity;
import com.dimon.ganwumei.ui.base.BaseActivity;
import com.dimon.ganwumei.ui.newsfeed.adapter.TabFragmentAdapter;
import com.dimon.ganwumei.ui.newsfeed.fragment.GanWuFragment;
import com.dimon.ganwumei.ui.newsfeed.fragment.GanWuListFragment;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements HasComponent<ActivityComponent>, NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tabLayout)
    TabLayout mTab;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.fabBtn)
    FloatingActionButton fabBtn;
    @Bind(R.id.rootLayout)
    CoordinatorLayout rootLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Nullable
    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBar;
    @Bind(R.id.navigation)
    NavigationView mNavigation;

    private ActivityComponent activityComponent;

    ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.e("czy");
        initUi();
        initializeToolbar();
        initializeTab();
        initializeInjector();
        initView();
    }

    private void initUi() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void initializeToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Gan物妹");
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
    }

    private void onToolbarClick() {

    }

    public boolean canBack() {
        return false;
    }


    private void initializeTab() {
        mTab.addTab(mTab.newTab().setText(Constant.ALL));
        mTab.addTab(mTab.newTab().setText(Constant.ANDROID));
        mTab.addTab(mTab.newTab().setText(Constant.IOS));
        List<String> tabList = new ArrayList<>();
        tabList.add(Constant.ALL);
        tabList.add(Constant.ANDROID);
        tabList.add(Constant.IOS);
        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < tabList.size(); i++) {
            if (i == 0) {
                Fragment fragment = new GanWuFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.FRAGMENT_TYPE, Constant.ALL);
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
            } else if (i == 1) {
                Fragment fragment = new GanWuListFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.FRAGMENT_TYPE, Constant.ANDROID);
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
            } else {
                Fragment fragment = new GanWuListFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.FRAGMENT_TYPE, Constant.IOS);
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
            }

        }
        TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        viewPager.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        mTab.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        mTab.setTabsFromPagerAdapter(fragmentAdapter);//给Tabs设置适配器
    }

    private void initializeInjector() {
        this.activityComponent = DaggerActivityComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    private void initView() {

        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.OPEN_DRAWER_CONTENT_DESC_RES
                , R.string.OPEN_DRAWER_CONTENT_DESC_RES);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                KLog.a(curDate);
                Intent intent = new Intent(MainActivity.this, GanDailyActivity.class);
                intent.putExtra(GanDailyActivity.EXTRA_GAN_DATE, curDate);
                startActivity(intent);
            }
        });

        mNavigation.setNavigationItemSelectedListener(this);
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_search:
                return true;
            case R.id.action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public ActivityComponent getComponent() {
        return activityComponent;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.navItem1:
                Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.navItem2:
                Toast.makeText(MainActivity.this, "呆毛我还没做收藏夹功能呢/(ㄒoㄒ)/~~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navItem3:
                Intent intent3 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent3);
                break;
            case R.id.navItem4:
                Toast.makeText(MainActivity.this, "这是呆毛我第一个练手小项目/(ㄒoㄒ)/~~谢谢支持~~！", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}