/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dimon.ganwumei.ui.newsfeed.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.ui.base.BaseActivity;
import com.dimon.ganwumei.util.Preconditions;
import com.dimon.ganwumei.util.ToastUtils;
import com.socks.library.KLog;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * WebActivity
 */
public class WebActivity extends BaseActivity {
    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_TITLE = "extra_title";
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.webView)
    WebView mWebView;
    @Bind(R.id.tv_title)
    TextSwitcher mTvTitle;
    @Bind(R.id.progressbar)
    ProgressBar mProgressbar;
    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    private String mUrl, mTitle;

    /**
     * Using newIntent trick, return WebActivity Intent, to avoid `public static`
     * constant
     * variable everywhere
     *
     * @return Intent to start WebActivity
     */
    public static Intent newIntent(Context context, String extraURL, String extraTitle) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, extraURL);
        intent.putExtra(EXTRA_TITLE, extraTitle);
        KLog.a(extraURL + extraTitle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);

        initToolbar();
        initSetting();
        KLog.a(mUrl);
        mWebView.loadUrl(mUrl);
        KLog.a(mWebView);
        mTvTitle.setFactory(() -> {
            TextView textView = new TextView(this);
            textView.setTextAppearance(this, R.style.WebTitle);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.postDelayed(() -> textView.setSelected(true), 1738);
            return textView;
        });
        mTvTitle.setInAnimation(this, android.R.anim.fade_in);
        mTvTitle.setOutAnimation(this, android.R.anim.fade_out);
        if (mTitle != null) setTitle(mTitle);
    }

    private void initToolbar() {
        if (mToolbar == null || mAppBarLayout == null) {
            throw new IllegalStateException("No toolbar");
        }

        mToolbar.setOnClickListener(v -> onToolbarClick());

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            mAppBarLayout.setElevation(10.6f);
        }

    }

    private void onToolbarClick() {

    }
    public void initSetting() {
        WebSettings settings = mWebView.getSettings();
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new MyClient());
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTvTitle.setText(title);
    }


    private void refresh() {
        mWebView.reload();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView != null) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();
                        } else {
                            finish();
                        }
                        return true;
                }
            }
            return super.onKeyDown(keyCode, event);
        }
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_copy_url:
                String copyDone = getString(R.string.tip_copy_done);
                Preconditions.copyToClipBoard(this, mWebView.getUrl(), copyDone);
                return true;
            case R.id.action_open_url:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(mUrl);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    ToastUtils.showLong(R.string.tip_open_fail);
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) mWebView.destroy();
        ButterKnife.unbind(this);
    }


    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }


    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressbar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressbar.setVisibility(View.GONE);
            } else {
                mProgressbar.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class MyClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                mWebView.getSettings().setLoadsImagesAutomatically(true);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            ToastUtils.showLong("Oh no! " + error.getDescription().toString());
        }
    }
}
