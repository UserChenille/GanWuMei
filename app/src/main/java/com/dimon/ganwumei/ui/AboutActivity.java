package com.dimon.ganwumei.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dimon.ganwumei.BuildConfig;
import com.dimon.ganwumei.R;
import com.dimon.ganwumei.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 关于
 * Created by Chenille on 2016/7/20.
 */
public class AboutActivity extends BaseActivity {
    @Bind(R.id.tv_version)
    TextView mTvVersion;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout mAppbar;
    @Bind(R.id.main_content)
    CoordinatorLayout mMainContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setUpVersionName();
        mCollapsingToolbar.setTitle("");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> AboutActivity.this.onBackPressed());
    }
    private void setUpVersionName() {
        mTvVersion.setText("Version " + BuildConfig.VERSION_NAME);
    }
}
