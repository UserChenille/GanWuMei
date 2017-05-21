package com.dimon.ganwumei.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.api.FileStore;
import com.dimon.ganwumei.api.thread.ThreadPoolManager;
import com.dimon.ganwumei.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 开始页面
 * Created by Chenille on 2016/7/20.
 */
public class AppStart extends BaseActivity {

    private static final String TAG = "AppStart";
    private static final long LOADING_TIME = 5000;
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_ENTER_HOME = 4;
    @Bind(R.id.iv_splash)
    ImageView ivSplash;
    Animation scaleAnimation;

    @Inject ThreadPoolManager threadPoolManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);
        ButterKnife.bind(this);
        startAnima();

        threadPoolManager.execute(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                //准备工作
                initApp();

                Looper.loop();
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goHome();
            }
        }, LOADING_TIME);
    }

    private void startAnima() {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(2000);
        animation.setFillAfter(true);
        ivSplash.startAnimation(animation);
    }

    /** 初始化工作 */
    private void initApp() {
        FileStore.INSTANCE.createFileFolder();
    }

    /** 启动 */
    private void goHome() {
        UiHelper.startToMainActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ivSplash.clearAnimation();
        ivSplash = null;
    }
}
