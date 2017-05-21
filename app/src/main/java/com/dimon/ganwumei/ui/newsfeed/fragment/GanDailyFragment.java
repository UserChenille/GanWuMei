package com.dimon.ganwumei.ui.newsfeed.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.database.DataManager;
import com.dimon.ganwumei.database.GanWuData;
import com.dimon.ganwumei.database.entities.News;
import com.dimon.ganwumei.injector.components.DaggerGanWuComponent;
import com.dimon.ganwumei.injector.modules.GanWuFragmentModule;
import com.dimon.ganwumei.ui.base.BaseFragment;
import com.dimon.ganwumei.ui.newsfeed.activity.GanDailyActivity;
import com.dimon.ganwumei.ui.newsfeed.adapter.GanDailyListAdapter;
import com.dimon.ganwumei.widget.MultiSwipeRefreshLayout;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 每日的干货信息
 * Created by Dimon on 2016/5/3.
 */
public class GanDailyFragment extends BaseFragment {

    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY = "day";
    int mYear, mMonth, mDay;
    List<News> mGanList;
    GanDailyListAdapter mAdapter;
    Subscription mSubscription;
    private boolean isPrepared;//标志位，标志已经初始化完成
    private boolean mHasLoadedOnce;//是否已被加载过一次，第二次就不再去请求数据了
    private boolean mIsRequestDataRefresh = false;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    MultiSwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.stub_empty_view)
    ViewStub mStubEmptyView;

    @Inject
    DataManager mDataManager;


    public static GanDailyFragment newInstance(int year, int month, int day) {
        GanDailyFragment fragment = new GanDailyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    public GanDailyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGanList = new ArrayList<>();
        mAdapter = new GanDailyListAdapter(mGanList);
        initInject();
        parseArguments();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void parseArguments() {
        Bundle bundle = getArguments();
        mYear = bundle.getInt(ARG_YEAR);
        mMonth = bundle.getInt(ARG_MONTH);
        mDay = bundle.getInt(ARG_DAY);
        KLog.a(mYear +"/" +mMonth+"/"+ mDay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, rootView);

        initRecyclerView();
        return rootView;
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trySetupSwipeRefresh();
        new Handler().postDelayed(() -> setRequestDataRefresh(true), 358);
        if (mGanList.size() == 0) loadData();
    }

    void trySetupSwipeRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_3,
                    R.color.refresh_progress_2, R.color.refresh_progress_1);
            // do not use lambda!!
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
    private void initInject() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.github.com")
//                .addConverterFactory(GsonConverterFactory.create()) //使用工厂模式创建Gason的解析器
//                .build();
//        GitHubService service = retrofit.create(GitHubService.class);

        GanDailyActivity activity = (GanDailyActivity) getActivity();
        DaggerGanWuComponent.builder()
                .ganWuFragmentModule(new GanWuFragmentModule())
                .activityComponent(activity.getComponent())
                .build()
                .inject(this);
    }

    private void loadData() {
        KLog.a(mDataManager);
        mSubscription = mDataManager.getGanWuData(mYear, mMonth, mDay)
                .map(data -> data.results)
                .map(this::addAllResults)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list ->{
                    if (list.isEmpty()) {
                        showEmptyView();
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    setRequestDataRefresh(false);
                    mHasLoadedOnce = true;
                }, throwable -> loadError(throwable));
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Snackbar.make(mRecyclerView, R.string.snap_load_fail,
                Snackbar.LENGTH_LONG).setAction(R.string.retry, v -> {
            requestDataRefresh();
            loadData();
        }).show();
    }

    private void showEmptyView() {
        mStubEmptyView.inflate();
    }

    private List<News> addAllResults(GanWuData.Result results) {
        if (results.androidList != null) mGanList.addAll(results.androidList);
        if (results.iOSList != null) mGanList.addAll(results.iOSList);
        if (results.appList != null) mGanList.addAll(results.appList);
        if (results.拓展资源List != null) mGanList.addAll(results.拓展资源List);
        if (results.瞎推荐List != null) mGanList.addAll(results.瞎推荐List);
        if (results.休息视频List != null) mGanList.addAll(0, results.休息视频List);
        return mGanList;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        loadData();
    }

    public Context context() {
        return this.getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
