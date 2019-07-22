package com.sz.dzh.drxjavasummary.module.rxlifecycle;

import com.sz.dzh.drxjavasummary.base.BaseView;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dengzh on 2017/9/11 0011.
 */

public class RxUtil {

    /**
     * 统一线程处理，且绑定生命周期
     * 用法： xxx .compose(RxUtil.rxSchedulerHelper(mView))
     * @param view
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T,T> rxSchedulerHelper(BaseView view){
        return new ObservableTransformer<T,T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())    //订阅在io线程
                        .unsubscribeOn(Schedulers.io())  //取消订阅在io线程，为啥要这个，不太清楚
                        .observeOn(AndroidSchedulers.mainThread()) //回调在主线程
                        .compose(view.bindToLifecycle());  //绑定生命周期
            }
        };
    }




}
