package com.sz.dzh.drxjavasummary.base;


import android.support.annotation.NonNull;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;

public interface BaseView {

     void showLoading();
     void dismissLoading();

     //为了让 IView 可以调用 RxLifeCycle的生命周期绑定
     <T> LifecycleTransformer<T> bindToLifecycle();
     <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event);

}
