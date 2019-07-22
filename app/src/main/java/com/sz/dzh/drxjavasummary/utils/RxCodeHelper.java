package com.sz.dzh.drxjavasummary.utils;

import android.content.Context;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dengzh on 2018/4/9.
 * 验证码帮助类
 * 倒计时和状态UI变化
 */

public class RxCodeHelper {

    private Context mContext;
    private TextView codeBt;
    private Disposable mDisposable;

    /**
     * 构造函数
     * @param mContext 上下文
     * @param codeBt   验证码的button
     */
    public RxCodeHelper(Context mContext, TextView codeBt) {
        this.mContext = mContext;
        this.codeBt = codeBt;
    }

    /**
     * 开启倒计时
     */
    public void start(){
        codeBt.setEnabled(false);
        //从0开始，走60个数，延迟是0s，周期为1次。
        Observable.intervalRange(0,60,0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        codeBt.setText((60 - aLong) + "s后可重发");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //倒计时完毕置为可点击状态
                        codeBt.setEnabled(true);
                        codeBt.setText("获取验证码");
                    }
                });
    }

    /**
     * 请求失败时，重置状态
     * 取消倒计时的订阅事件
     */
    public void reset(){
        if(mDisposable!=null){
            mDisposable.dispose();
        }
        codeBt.setEnabled(true);
        codeBt.setText("获取验证码");
    }

    /**
     * 界面销毁时，取消倒计时订阅事件
     */
    public void stop(){
        if(mDisposable!=null){
            mDisposable.dispose();
        }
    }

}
