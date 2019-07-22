package com.sz.dzh.drxjavasummary.module.rxStudy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by administrator on 2018/11/9.
 * 条件/布尔操作符
 * 1.all()         判断发送的每项数据是否都满足设置的函数条件,若满足，返回 true；否则，返回 false。
 * 2.takeWhile（）  判断发送的每项数据是否满足设置函数条件，条件 = true时，才发送Observable的数据，当为false后，再也不发生事件
 * 3.skipWhile（）  判断发送的每项数据是否满足设置函数条件，条件 = false时，才发送Observable的数据
 * 4.takeUntil（）  执行到某个条件时，停止发送事件
 * 5.skipUntil()    和takeUntil相反
 * 6.sequenceEqual（）
 * 7.contains（）
 * 8.isEmpty（）
 * 9.amb（）         当需要发送多个 Observable时，只发送 先发送数据的Observable的数据，而其余 Observable则被丢弃。
 */
public class RxConditionActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_condition);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("条件/布尔操作符");
    }

    @OnClick({R.id.btn_all, R.id.btn_takeWhile, R.id.btn_skipWhile, R.id.btn_takeUntil, R.id.btn_skipUntil, R.id.btn_sequenceEqual, R.id.btn_contains, R.id.btn_amb, R.id.btn_defaultEmpty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_all:
                all();
                break;
            case R.id.btn_takeWhile:
                takeWhile();
                break;
            case R.id.btn_skipWhile:
                skipWhile();
                break;
            case R.id.btn_takeUntil:
                takeUntil();
                break;
            case R.id.btn_skipUntil:
                skipUntil();
                break;
            case R.id.btn_sequenceEqual:
                sequenceEqual();
                break;
            case R.id.btn_contains:
                contains();
                break;
            case R.id.btn_amb:
                amb();
                break;
            case R.id.btn_defaultEmpty:
                defaultEmpty();
                break;
        }
    }


    /**
     * all()
     * 作用： 判断发送的每项数据是否都满足 设置的函数条件
     * 若满足，返回 true；否则，返回 false
     *
     * 例子：判断是否都小于10
     * 打印结果：
     * true
     */
    private void all() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return (integer <= 10);
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                KLog.d(TAG, "接收事件：" + aBoolean);
            }
        });
    }

    /**
     * takeWhile（）
     * 作用:  判断发送的每项数据是否满足 设置函数条件
     * 若发送的数据满足该条件，则发送该项数据；否则不再发送
     *
     * 例子：小于3的发送
     * 打印结果：
     * 0、1、2
     */
    private void takeWhile() {
        //[0,5)，每秒递增1
        Observable.intervalRange(0,5,0,1,TimeUnit.SECONDS)
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong < 3; //为true发送
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                KLog.d(TAG, "接收事件：" + aLong);
            }
        });
    }


    /**
     * skipWhile()
     * 作用: 判断发送的每项数据是否满足 设置函数条件
     * 直到该判断条件 = false时，才开始发送Observable的数据
     *
     * 例子：小于3的跳过
     * 打印结果：
     * 3、4
     */
    private void skipWhile() {
        Observable.intervalRange(0,5,0,1,TimeUnit.SECONDS)
                .skipWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong < 3; //为true跳过，不发送
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                KLog.d(TAG, "接收事件：" + aLong);
            }
        });
    }

    /**
     * takeUntil（）
     * 发射事件，直到xxx时停止发射
     * Observable1（调用takUtil的Observable）发送数据，当Observable2（takUtil参数中的Observable）发送数据时，两个Obserable会同时取消订阅。
     *
     * 例子：事件2，2.5s后发送，同时取消订阅。
     * 打印结果：
     * 0、1、2
     */
    private void takeUntil() {
        Observable.intervalRange(0,5,0,1,TimeUnit.SECONDS)
                .takeUntil(Observable.timer(2500, TimeUnit.MILLISECONDS))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        KLog.d(TAG, "接收事件：" + aLong);
                    }
                });
    }


    /**
     * skipUntil（）
     * 跳过，直到xxx时才发射事件
     * 等到 skipUntil（）传入的Observable开始发送数据，（原始）第1个Observable的数据才开始发送数据
     *
     * 例子：事件2，2.5s后发送。
     * 打印结果：
     * 3s后才开始打印 3、4
     */
    private void skipUntil() {
        Observable.intervalRange(0,5,0,1,TimeUnit.SECONDS)
                .skipUntil(Observable.timer(3, TimeUnit.SECONDS))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        KLog.d(TAG, "接收事件：" + aLong);
                    }
                });
    }


    /**
     * SequenceEqual（）
     * 作用: 判定两个Observables需要发送的数据是否相同
     * 若相同，返回 true；否则，返回 false
     *
     * 打印结果：
     * true
     */
    private void sequenceEqual() {
        Observable.sequenceEqual(Observable.just(3, 4, 5), Observable.just(3, 4, 5))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        KLog.d(TAG, "接收事件：" + aBoolean);
                    }
                });
    }

    /**
     * contains（）
     * 判断发送的数据中是否包含指定数据
     *
     * 打印结果：
     * true
     */
    private void contains() {
        Observable.just(3, 4, 5)
                .contains(3)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        KLog.d(TAG, "接收事件：" + aBoolean);
                    }
                });
    }

    /**
     * amb()
     * 作用： 当需要发送多个 Observable时，只发送 先发送数据的Observable的数据，而其余 Observable则被丢弃。
     *
     * 打印结果：
     * 4、5、6
     */
    private void amb() {
        // 设置2个需要发送的Observable & 放入到集合中
        List<ObservableSource<Integer>> list = new ArrayList<>();
        // 第1个Observable延迟1秒发射数据
        list.add(Observable.just(1, 2, 3).delay(1, TimeUnit.SECONDS));
        // 第2个Observable正常发送数据
        list.add(Observable.just(4, 5, 6));

        // 一共需要发送2个Observable的数据
        // 但由于使用了amb(),所以仅发送先发送数据的Observable,也就是第2个Observable
        Observable.amb(list).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                KLog.d(TAG, "接收事件：" + integer);
            }
        });
    }

    /**
     * defaultIfEmpty（）
     * 作用:
     * 在不发送任何有效事件（ Next事件）、仅发送了 Complete 事件的前提下，发送一个默认值
     *
     * 打印结果：
     * 10
     */
    private void defaultEmpty() {
        //empty()就是只发 onComplete()事件
        /*Observable.empty().defaultIfEmpty(10).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                KLog.d(TAG, "接收事件：" + o.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                KLog.d(TAG, "onComplete");
            }
        });*/

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 不发送任何有效事件
                // emitter.onNext(1);
                // 仅发送Complete事件
                emitter.onComplete();
            }
        }).defaultIfEmpty(10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        KLog.d(TAG, "接收事件：" + integer);
                    }
                });
    }



}
