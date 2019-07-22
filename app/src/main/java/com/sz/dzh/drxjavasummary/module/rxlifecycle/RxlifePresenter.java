package com.sz.dzh.drxjavasummary.module.rxlifecycle;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.base.BasePresenterImpl;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by administrator on 2018/12/3.
 * 模拟一个网络请求
 */
public class RxlifePresenter extends BasePresenterImpl<RxlifecContract.View> implements RxlifecContract.Presenter{

    /**
     * 模拟网络请求,3s后才回调,在onDestroy时取消订阅
     *
     * 结果：
     * 在onPauese()状态下继续执行
     * 在onDestroy()状态下取消了订阅
     */
    @Override
    public void onTest1() {
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())  //订阅在io线程
                .observeOn(AndroidSchedulers.mainThread()) //回调在主线程
                .compose(mView.bindUntilEvent(ActivityEvent.DESTROY)) //指定在onDestroy销毁
               // .compose(mView.bindToLifecycle())   //取消订阅交由RxLifeCycle来判断
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        KLog.d("onTest1Success()回调成功");
                        mView.onTest1Success("onTest1 请求成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoading();
                    }
                });
    }

    /**
     * 模拟网络请求,3s后才回调
     *
     * 结果：
     * onCreate()订阅的，在onDestroy()取消订阅
     * onResume()订阅的，在onPause()取消订阅
     */
    @Override
    public void onTest2() {
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())  //订阅在io线程
                .observeOn(AndroidSchedulers.mainThread()) //回调在主线程
                .compose(mView.bindToLifecycle()) //直接绑定声明周期，onCreate()订阅的，在onDestroy()取消订阅；在onResume()订阅的，在onPause()取消订阅
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        KLog.d("onTest2Success()回调成功");
                        mView.onTest2Success("onTest2 请求成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.dismissLoading();
                    }
                });
    }
}
