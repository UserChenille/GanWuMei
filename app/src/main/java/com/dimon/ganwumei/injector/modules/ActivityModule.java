package com.dimon.ganwumei.injector.modules;

import android.content.Context;

import com.dimon.ganwumei.injector.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * 把activity暴露给相关联的类。
 * Created by Chenille on 2016/7/29.
 */
@Module
public class ActivityModule {
    private final Context mContext;

    public ActivityModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    @ActivityScope
    Context provideActivityContext() {
        return mContext;
    }

}
