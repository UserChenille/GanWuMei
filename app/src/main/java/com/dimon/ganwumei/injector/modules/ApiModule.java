package com.dimon.ganwumei.injector.modules;


import com.dimon.ganwumei.MyApplication;
import com.dimon.ganwumei.network.RestAPI;
import com.dimon.ganwumei.util.FileUtils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmObject;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 提供网络接口
 * Created by Chenille on 2016/7/29.
 */
@Module
public class ApiModule {
    private MyApplication mApplication;

    public ApiModule(MyApplication mApplication) {
        this.mApplication = mApplication;
    }
    public static final String BASE_URL = "http://gank.io/api/";


    private OkHttpClient okHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // config log
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) //设置出现错误进行重新连接。
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(logging)   //拦截器
                .cache(providesCache())
                .build();
    }

    @Provides
    Cache providesCache() {
        File httpCacheFile = FileUtils.getDiskCacheDir("response");
        return new Cache(httpCacheFile, 1024 * 100 * 1024);
    }

    @Provides
    @Singleton
    RestAPI provideRestAPI() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient())
                .build();

        return retrofit.create(RestAPI.class);
    }
}
