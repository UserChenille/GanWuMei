package com.dimon.ganwumei.injector.components;

import android.content.Context;

import com.dimon.ganwumei.database.DataManager;
import com.dimon.ganwumei.injector.modules.ApiModule;
import com.dimon.ganwumei.injector.modules.AppModule;
import com.dimon.ganwumei.injector.qualifier.ContextType;

import javax.inject.Singleton;

import dagger.Component;

/**
 * APP组件，可以理解为一个应用级别的注入器，生命周期跟Application一样的组件，是@Inject和@Module的桥梁
 * Created by Chenille on 2016/7/29.
 */
@Singleton //使用了@Singleton注解，使其保证唯一性
@Component(modules = {AppModule.class, ApiModule.class})

public interface AppComponent {

    @ContextType("application")
    Context context();

    DataManager DATA_MANAGER();
}
