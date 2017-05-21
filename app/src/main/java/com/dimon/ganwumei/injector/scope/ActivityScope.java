package com.dimon.ganwumei.injector.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * //一个自定义的范围注解,注解对象的生命周期应该遵循activity的生命周期
 * Created by Chenille on 2016/7/29.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
