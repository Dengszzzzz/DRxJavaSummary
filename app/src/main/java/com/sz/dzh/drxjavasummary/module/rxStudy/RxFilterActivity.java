package com.sz.dzh.drxjavasummary.module.rxStudy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by administrator on 2018/11/8.
 * 过滤操作符
 * filter（）                         过滤 特定条件的事件
 * ofType（）                         过滤 只返回特定数据类型的数据
 * skip（） / skipLast（）             跳过n个事件 / 跳过最后的n个事件
 * distinct（） / distinctUntilChanged（）     过滤重复事件 / 只确保相邻元素不重复出现
 * take（） / takeLast（）             接收n个事件 / 接收最后发送的n个事件
 *
 */
public class RxFilterActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_filter);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("过滤操作符");
    }

    @OnClick({R.id.btn_filter, R.id.btn_ofType, R.id.btn_skip, R.id.btn_skipLast, R.id.btn_distinct,
            R.id.btn_distinctUntilChanged, R.id.btn_take, R.id.btn_takeLast,R.id.btn_throttleFirst,R.id.btn_debounce})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_filter:
                filter();
                break;
            case R.id.btn_ofType:
                ofType();
                break;
            case R.id.btn_skip:
                skip();
                break;
            case R.id.btn_skipLast:
                skipLast();
                break;
            case R.id.btn_distinct:
                distinct();
                break;
            case R.id.btn_distinctUntilChanged:
                distinctUntilChanged();
                break;
            case R.id.btn_take:
                take();
                break;
            case R.id.btn_takeLast:
                takeLast();
                break;
            case R.id.btn_throttleFirst:
                throttleFirst();
                break;
            case R.id.btn_debounce:
                debounce();
                break;
        }
    }

    /**
     * filter()
     * 过滤数据，Observer接收过滤后数据
     *
     * 打印结果：
     * 3,4
     */
    private void filter(){
        Observable.just(1,2,3,4).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer>2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                KLog.d(TAG,"接收事件：" + integer);
            }
        });
    }

    /**
     * ofType()
     * 过滤类型
     *
     * 打印结果：
     * 哈哈哈、哦哦
     */
    private void ofType(){
        Object[] data = {1,2,"哈哈哈",3,"哦哦"};
        Observable.fromArray(data).ofType(String.class).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                KLog.d(TAG,"接收事件：" + s);
            }
        });
    }

    /**
     * skip()
     * 从前往后，跳过n个
     *
     * 打印结果：
     * 3，4
     */
    private void skip(){
        Observable.just(1,2,3,4).skip(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                KLog.d(TAG,"接收事件：" + integer);
            }
        });
    }

    /**
     * skipLast()
     * 从后往前，跳过n个
     *
     * 打印结果：
     * 1，2
     */
    private void skipLast(){
        Observable.just(1,2,3,4).skipLast(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                KLog.d(TAG,"接收事件：" + integer);
            }
        });
    }


    /**
     * distinct()
     * 过滤掉重复元素
     *
     * 打印结果：
     * 1、2、3、4
     */
    private void distinct(){
        Observable.just(1,2,2,3,3,4,2,3).distinct().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                KLog.d(TAG,"接收事件：" + integer);
            }
        });
    }

    /**
     * distinctUntilChanged()
     * 只确保相邻元素不重复出现
     *
     * 打印结果：
     * 1、2、3、4、2、3
     */
    private void distinctUntilChanged(){
        Observable.just(1,2,2,3,3,4,2,3).distinctUntilChanged().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                KLog.d(TAG,"接收事件：" + integer);
            }
        });
    }


    /**
     * take()
     * 接收n个事件
     *
     * 打印结果：
     * 1、2、3
     */
    private void take(){
        Observable.just(1,2,3,4).take(3).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                KLog.d(TAG,"接收事件：" + integer);
            }
        });
    }


    /**
     * takeLast()
     * 接收最后发送的n个事件
     *
     * 打印结果：
     * 2、3、4
     */
    private void takeLast(){
        Observable.just(1,2,3,4).takeLast(3).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                KLog.d(TAG,"接收事件：" + integer);
            }
        });
    }


    /**
     * throttleFirst()
     * 一段时间内，只接收发射的第一个事件
     *
     * 实际应用：
     * 需要使用RxBinding, 处理短时间多次点击按钮
     *
     * 打印结果:
     * 0、6、12。。。。
     */
    private void throttleFirst() {
        //每1s发送事件 , 5s内只会发送一次
        Observable.interval(1, TimeUnit.SECONDS)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        KLog.d(TAG,"接收事件：" + aLong);
                    }
                });
    }

    /**
     * debounce()
     * 一段时间内没有操作，才执行一次操作。
     * 例子：发射1，休眠1s，发射2，休眠2s，发射3，休眠3s。debounce设置2.5s空闲才发射事件。所以只有事件3发射了
     *
     * 打印结果：
     * 接收事件：3
     *
     * 实际应用：
     * 联网搜索，EditText监听，当输入改变一段事件后，发起请求。用debounce(n)操作符，只有当用户输入关键字后n毫秒才发射数据。
     * 需要使用RxBinding
     */
    private void debounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Thread.sleep(1000);
                emitter.onNext(2);
                Thread.sleep(2000);
                emitter.onNext(3);
                Thread.sleep(3000);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .debounce(2500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        KLog.d(TAG,"接收事件：" + integer);
                    }
                });
    }


}
