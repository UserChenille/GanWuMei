package com.dimon.ganwumei.ui.newsfeed.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.api.Constant;
import com.dimon.ganwumei.database.DataManager;
import com.dimon.ganwumei.database.entities.Item;
import com.dimon.ganwumei.database.entities.News;
import com.dimon.ganwumei.injector.components.DaggerAppComponent;
import com.dimon.ganwumei.injector.components.DaggerGanWuComponent;
import com.dimon.ganwumei.injector.modules.GanWuFragmentModule;
import com.dimon.ganwumei.ui.base.BaseFragment;
import com.dimon.ganwumei.ui.newsfeed.activity.MainActivity;
import com.dimon.ganwumei.ui.newsfeed.adapter.GanWuListAdapter;
import com.dimon.ganwumei.util.RandomDatatToItemsMapper;
import com.dimon.ganwumei.widget.MultiSwipeRefreshLayout;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 每日随机干货
 * Created by Dimon on 2016/4/20.
 */
public class GanWuListFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private Realm mRealm;
    private View view;
    private List<Item> mNewsList;
    private String mGanWuType = "";
    private LinearLayoutManager linearLayoutManager;
    protected Subscription subscription;
    private GanWuListAdapter mGanWuListAdapter;
    private boolean mIsRequestDataRefresh = false;
    private boolean isPrepared;    // 标志位，标志已经初始化完成
    private boolean mHasLoadedOnce;    // 是否已被加载过一次，第二次就不再去请求数据了

    @Inject
    DataManager mDataManager;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInject();
    }
    private void initInject() {
        MainActivity activity = (MainActivity) getActivity();


        DaggerGanWuComponent.builder()
                .ganWuFragmentModule(new GanWuFragmentModule())
                .activityComponent(activity.getComponent())
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
            //获取索引值
            Bundle bundle = getArguments();
            if (bundle != null) {
                mGanWuType = bundle.getString(Constant.FRAGMENT_TYPE);
                KLog.a(mGanWuType);
            }
        }
        ButterKnife.bind(this, view);
        mRealm = Realm.getDefaultInstance();
        mNewsList = new ArrayList<>();

        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trySetupSwipeRefresh();
        new Handler().postDelayed(() -> setRequestDataRefresh(true), 358);
        loadData(false);
        KLog.a(mRecyclerView);
        linearLayoutManager = new LinearLayoutManager(context());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mGanWuListAdapter = new GanWuListAdapter(mNewsList, context());
        mRecyclerView.setAdapter(mGanWuListAdapter);
    }

    private void loadData(boolean clean) {
//        KLog.a(mDataManager);
        if (TextUtils.equals(mGanWuType, Constant.ANDROID)) {
            subscription = mRealm
                    .where(News.class)
                    .findAllSortedAsync("publishedAt")
                    .asObservable()
                    .filter(newses -> newses.isLoaded())
                    .flatMap(newses1 ->
                            mDataManager.getRandomData(Constant.ANDROID, "20")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()))
                    .map(RandomDatatToItemsMapper.getInstance())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newsess -> {
                        if (clean) mNewsList.clear();
                        mGanWuListAdapter.updateItems(newsess,true);
                        setRequestDataRefresh(false);
                        mHasLoadedOnce = true;
                    }, throwable -> loadError(throwable));
            addSubscription(subscription);
        } else {
            subscription = mRealm
                    .where(News.class)
                    .findAllSortedAsync("publishedAt")
                    .asObservable()
                    .filter(newses -> newses.isLoaded())
                    .flatMap(newses1 ->
                            mDataManager.getRandomData(Constant.IOS, "20")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()))
                    .map(RandomDatatToItemsMapper.getInstance())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newsess -> {
                        if (clean) mNewsList.clear();
                        mGanWuListAdapter.updateItems(newsess,true);
                        setRequestDataRefresh(false);
                        mHasLoadedOnce = true;
                    }, throwable -> loadError(throwable));
            addSubscription(subscription);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Snackbar.make(mRecyclerView, R.string.snap_load_fail,
                Snackbar.LENGTH_LONG).setAction(R.string.retry, v -> {
            requestDataRefresh();
            loadData(true);
        }).show();
    }

    void trySetupSwipeRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_3,
                    R.color.refresh_progress_2, R.color.refresh_progress_1);
            mSwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            requestDataRefresh();
                        }
                    });
        }
    }

    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }

    public void setRequestDataRefresh(boolean requestDataRefresh) {
        if (mSwipeRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            // 防止刷新消失太快，让子弹飞一会儿.
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 1000);
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    public Context context() {
        return this.getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        loadData(false);
    }
}
