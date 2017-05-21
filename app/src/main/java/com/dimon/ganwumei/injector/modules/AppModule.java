package com.dimon.ganwumei.injector.modules;

import android.content.Context;

import com.dimon.ganwumei.MyApplication;
import com.dimon.ganwumei.injector.qualifier.ContextType;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * AppModule作用为提供在应用的生命周期中存活的对象
 * Created by Chenille on 2016/7/29.
 */
@Module
public class AppModule {

    private MyApplication mApplication;

    public AppModule(MyApplication application){
        this.mApplication = application;
    }

    @Provides
    @Singleton
    MyApplication provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    @ContextType("application")
    Context provideContext(){
        return MyApplication.getApplication();
    }
}
