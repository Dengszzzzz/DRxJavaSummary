package com.sz.dzh.drxjavasummary.module.rxStudy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by administrator on 2018/11/5.
 * RxJava2 创建操作符
 * 包括：完整&快速创建被观察者，定时操作，周期性操作，数据/集合遍历
 * 基本创建： create()
 * 快速创建： just(),fromArray(),fromIterable(),never(),empty(),error()
 * 延迟创建： defer(),timer(),interval(),intervalRange(),range(),rangeLong()
 * <p>
 * <p>
 * just()           最多发送10个参数。
 * fromArray()      传入一个数组，长度不限制
 * fromIterable()   传入list集合
 * never()          不发送事件
 * empty()          仅发送complete事件
 * error()          仅发送empty事件
 * defer()          订阅才创建Observable。
 * timer()          延迟一定时间才发送。
 * interval()       周期性发送，从0开始+。举例：用来做轮询请求
 * intervalRange()  指定范围，周期性发送。举例：验证码倒计时
 * range()          连续发送一个时间序列，可指定范围。
 */
public class RxCreateActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_create);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("创建操作符");
    }

    @OnClick({R.id.btn_create, R.id.btn_just, R.id.btn_fromArray, R.id.btn_fromIterable, R.id.btn_defer, R.id.btn_timer, R.id.btn_interval, R.id.btn_intervalRange, R.id.btn_range})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                create();
                break;
            case R.id.btn_just:
                just();
                break;
            case R.id.btn_fromArray:
                fromArray();
                break;
            case R.id.btn_fromIterable:
                fromIterable();
                break;
            case R.id.btn_defer:
                defer();
                break;
            case R.id.btn_timer:
                timer();
                break;
            case R.id.btn_interval:
                interval();
                break;
            case R.id.btn_intervalRange:
                intervalRange();
                break;
            case R.id.btn_range:
                range();
                break;
        }
    }

    /**
     * 普通创建
     * Observable：被观察者，发送事件
     * Observer：观察者，接收事件，它里面有4个方法。
     * subscribe：订阅
     * ObservableEmitter：事件发射器。作用是定义需要发送的事件 & 向观察者发送事件
     * Disposable：可切断操作。
     *
     * 结果：
     * 执行完onComplete()后，不再接收事件
     */
    private void create() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                //定义要发送的事件。
                //ObservableEmitter类，事件发射器。作用是定义需要发送的事件 & 向观察者发送事件
                KLog.d(TAG, "Observable emitter 1" + "\n");
                emitter.onNext(1);
                KLog.d(TAG, "Observable emitter 2" + "\n");
                emitter.onNext(2);
                KLog.d(TAG, "Observable emitter 3" + "\n");
                emitter.onNext(3);
                KLog.d(TAG, "Observable onComplete" + "\n");
                emitter.onComplete();
                KLog.d(TAG, "Observable emitter 4" + "\n");
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            //观察者(下游)，接收事件
            private Disposable mDisposable;  //可切断操作

            /**
             * 订阅成功就执行的方法
             * @param d  可切断操作
             */
            @Override
            public void onSubscribe(Disposable d) {
                KLog.d(TAG, "onSubscribe: " + d.isDisposed() + "\n");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                KLog.d(TAG, "onNext: " + integer + "\n");
                //可以切断，让其不再接受上游操作
                //mDisposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                KLog.d(TAG, "onError: " + e.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                //当执行onComplete后，下游不再接收上游操作
                KLog.d(TAG, "onComplete");
            }
        });
    }

    /**
     * just（）
     * 作用
     * 1.快速创建1个被观察者对象（Observable）
     * 2.发送事件的特点：直接发送 传入的事件
     * <p>
     * 应用场景
     * 快速创建 被观察者对象（Observable） & 发送10个以下事件
     */
    private void just() {
        Observable.just(1, 2, 3, 4,5,6,7,8,9,10)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "onNext:  " + integer + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * fromArray（）
     * 作用
     * 1.快速创建1个被观察者对象（Observable）
     * 2.发送事件的特点：直接发送 传入的数组数据
     * <p>
     * 会将数组中的数据转换为Observable对象
     * 应用场景
     * 1.快速创建 被观察者对象（Observable） & 发送10个以上事件（数组形式）
     * 2.数组元素遍历
     */
    private void fromArray() {
        Integer[] items = {0, 1, 2, 3, 4,5,6,7,8,9,10,11};
        Observable.fromArray(items)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "onNext :  " + integer + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * fromIterable()
     * 作用
     * 1.快速创建1个被观察者对象（Observable）
     * 2.发送事件的特点：直接发送 传入的集合List数据
     * <p>
     * 会将数组中的数据转换为Observable对象
     * 应用场景:
     * 1.快速创建 被观察者对象（Observable） & 发送10个以上事件（集合形式）
     * 2.集合元素遍历
     */
    private void fromIterable() {
        //快速创建 被观察者对象（Observable） & 发送10个以上事件（集合形式）
        //集合元素遍历
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        Observable.fromIterable(list)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "onNext : " + integer + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }




    /**
     * defer（）
     * 作用:
     * 直到有观察者（Observer ）订阅时，才动态创建被观察者对象（Observable） & 发送事件
     * <p>
     * 应用场景:
     * 动态创建被观察者对象（Observable） & 获取最新的Observable对象数据
     */
    private Integer ii = 10;  //第一次赋值
    private void defer() {
        //通过defer 定义被观察者对象，此时被观察者对象还没创建
        //如果用Observable<Integer> observable = Observable.just(ii)，打印的是10
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(ii);
            }
        });
        //第二次赋值
        ii = 15;
        //观察者开始订阅,此时才会调用defer创建被观察者
        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                KLog.d(TAG, "onNext : " + integer + "\n");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * timer（）
     * 作用
     * 1.快速创建1个被观察者对象（Observable）
     * 2.发送事件的特点：延迟指定时间后，发送1个数值0（Long类型）
     * 本质 = 延迟指定时间后，调用一次 onNext(0)
     * 应用场景:
     * 延迟指定事件，发送一个0，一般用于检测
     */
    private void timer() {
        //timer操作符默认运行在一个新线程上,也可自定义线程调度
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        //会返回0
                        Log.d(TAG, "onNext : " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * interval（）
     * 作用
     * 1.快速创建1个被观察者对象（Observable）
     * 2.发送事件的特点：每隔指定时间 就发送 事件
     * 发送的事件序列 = 从0开始、无限递增1的的整数序列
     */
    private Disposable mintervalDispb;
    private void interval() {
        // 参数1 = 第1次延迟时间；
        // 参数2 = 间隔时间数字；
        // 参数3 = 时间单位；
        //interval默认在computation调度器上执行
        Observable.interval(3, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mintervalDispb = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext : " + aLong);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * intervalRange（）
     * 作用
     * 1.快速创建1个被观察者对象（Observable）
     * 2.发送事件的特点：每隔指定时间 就发送 事件，可指定发送的数据的数量
     * <p>
     * 发送的事件序列 = 从0开始、无限递增1的的整数序列 ,作用类似于interval（），但可指定发送的数据的数量
     */
    private void intervalRange() {
        // 参数说明：
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        // 参数3 = 第1次事件延迟发送时间；
        // 参数4 = 间隔时间；
        // 参数5 = 时间单位;
        // 该例子发送的事件序列特点：
        // 1. 从3开始，一共发送10个事件；
        // 2. 第1次延迟2s发送，之后每隔1秒产生1个数字
        Observable.intervalRange(3, 10, 2, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext : " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * range（）
     * 作用
     * 1.快速创建1个被观察者对象（Observable）
     * 2.发送事件的特点：连续发送 1个事件序列，可指定范围
     * <p>
     * 发送的事件序列 = 从0开始、无限递增1的的整数序列,作用类似于intervalRange（），但区别在于：无延迟发送事件
     */
    private void range() {
        //感觉和发数组差不多
        Observable.range(3, 10)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext : " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mintervalDispb!=null){
            mintervalDispb.dispose();
        }
    }


}
