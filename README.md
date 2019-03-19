一个使用Dagger2+Retrofit2+RxJava+Reaml搭建的练手项目，API接口由gank.io提供，本是对自己学习的一个记录。

如果有人正好在学习这些框架，可以下载下来用于参照（不用客气^_^）。

本项目主要是一个对一些流行开源库如何使用的学习，故能用到的地方都选用了比较成熟的开源库

如下：

    compile 'com.github.chrisbanes:PhotoView:1.2.6'

    compile 'com.jakewharton:butterknife:7.0.1'// 标注
    compile 'com.jakewharton:disklrucache:2.0.2'//缓存

    apt 'com.google.dagger:dagger-compiler:2.0.2' //指定注解处理器
    compile 'com.google.dagger:dagger:2.0.2' // dagger2公用api
    compile 'com.google.dagger:dagger-compiler:2.0.2' // dagger2

    compile 'io.reactivex:rxandroid:1.1.0' // RxAndroid
    compile 'io.reactivex:rxjava:1.1.0' // 推荐同时加载RxJava

    compile 'com.squareup.retrofit2:retrofit:2.0.0-beta4' // Retrofit网络处理
    compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0-beta4' // Retrofit的rx解析库
    compile 'com.squareup.retrofit2:converter-gson:2.0.0-beta4' // Retrofit的gson库

    compile 'com.google.code.gson:gson:2.6.2'

    compile 'com.github.bumptech.glide:glide:3.7.0'

    compile 'com.squareup.okhttp3:okhttp:3.1.2'
    compile 'com.squareup.okhttp3:logging-interceptor:3.1.2'

    provided 'javax.annotation:jsr250-api:1.0' // Java标注
    provided 'org.glassfish:javax.annotation:10.0-b28'//添加android缺失的部分javax注解

    compile 'com.github.zhaokaiqiang.klog:library:1.3.0'//日志输出

    compile 'org.greenrobot:eventbus:3.0.0'
