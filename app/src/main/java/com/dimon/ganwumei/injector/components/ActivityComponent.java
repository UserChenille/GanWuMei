package com.dimon.ganwumei.injector.components;

import android.content.Context;

import com.dimon.ganwumei.database.DataManager;
import com.dimon.ganwumei.injector.scope.ActivityScope;
import com.dimon.ganwumei.injector.modules.ActivityModule;
import com.dimon.ganwumei.ui.base.BaseActivity;
import com.dimon.ganwumei.ui.newsfeed.activity.GanDailyActivity;
import com.dimon.ganwumei.ui.newsfeed.activity.MainActivity;

import dagger.Component;

/**
 * Activity组件，可以理解为一个Activity级别的注入器，生命周期跟Activity一样的组件，是@Inject和@Module的桥梁
 * Created by Chenille on 2016/7/29.
 */
@ActivityScope //一个自定义的范围注解,生命周期应该遵循activity的生命周期
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    //暴露Activity
    void inject(BaseActivity baseActivity);
    void inject(MainActivity mainActivity);
    void inject(GanDailyActivity ganDailyActivity);

    Context CONTEXT();
    DataManager DATA_MANAGER();
}
