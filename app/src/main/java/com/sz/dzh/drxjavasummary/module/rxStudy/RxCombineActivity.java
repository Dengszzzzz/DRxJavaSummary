package com.sz.dzh.drxjavasummary.module.rxStudy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;
import com.sz.dzh.drxjavasummary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by administrator on 2018/11/6.
 * 组合/合并操作符
 * 组合多个被观察者：
 * concat（） / concatArray（）   组合多个被观察者一起发送数据，合并后按发送顺序串行执行
 * merge（） / mergeArray（）     组合多个被观察者一起发送数据，合并后按时间线并行执行
 * concatDelayError（） / mergeDelayError（）    onError事件推迟到其他被观察者发送事件结束后才触发
 *
 * 合并多个事件：
 * zip（）                        合并多个被观察者（Observable）发送的事件
 * combineLatest()                和最新事件合并
 * reduce()                       把被观察者需要发送的事件聚合成1个事件
 * collect()                      将被观察者Observable发送的数据事件收集到一个数据结构里
 *
 * 其他：
 * startWith()                    在一个被观察者发送事件前，追加发送一些数据 / 一个新的被观察者
 * count()                        统计被观察者发送事件的数量
 */
public class RxCombineActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_combine);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("组合 / 合并操作符");
    }

    @OnClick({R.id.btn_concat, R.id.btn_merge, R.id.btn_concatDelayError, R.id.btn_zip, R.id.btn_combineLatest, R.id.btn_reduce, R.id.btn_collect, R.id.btn_startWith, R.id.btn_count})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_concat:
                concat();
                break;
            case R.id.btn_merge:
                merge();
                break;
            case R.id.btn_concatDelayError:
                concatDelayError();
                break;
            case R.id.btn_zip:
                zip();
                break;
            case R.id.btn_combineLatest:
                combineLatest();
                break;
            case R.id.btn_reduce:
                reduce();
                break;
            case R.id.btn_collect:
                collect();
                break;
            case R.id.btn_startWith:
                startWith();
                break;
            case R.id.btn_count:
                ToastUtils.showToast("统计被观察者发送事件的数量");
                break;
        }
    }


    /**
     * concat（） / concatArray（）
     * 作用:组合多个被观察者一起发送数据，合并后按发送顺序串行执行
     * 区别：组合被观察者的数量，即concat（）组合被观察者数量≤4个，而concatArray（）则可＞4个
     */
    private void concat() {
        Observable.concat(Observable.just(1, 2, 3),
                Observable.just(4, 5, 6),
                Observable.just(7, 8, 9),
                Observable.just(10, 11, 12))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "接收到了事件" + integer);
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
     * merge（） / mergeArray（）
     * 作用：
     * 组合多个被观察者一起发送数据，合并后按时间线并行执行
     *
     * 打印结果：
     * Observable1 和 Observable2 数据掺杂在一起
     */
    private void merge() {
        Observable.merge(
                //从0开始，发3个，1s后开始，每秒1个
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS),
                //从10开始，发5个，1s后开始，每秒1个
                Observable.intervalRange(10, 5, 1, 1, TimeUnit.SECONDS))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        KLog.d(TAG, "接收到了事件" + aLong);
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
     * concatDelayError（） / mergeDelayError（）
     * 问题：使用concat()和merge()操作符时，若其中1个被观察者发出onError事件，则会马上终止其他被观察者继续发送事件
     * 解决方法：
     * 希望onError事件推迟到其他被观察者发送事件结束后才触发。
     *
     * 打印结果：
     * 完成Observable2的事件接受完毕后，执行onError。要看区别可以把concatArrayDelayError 改成 concatArray
     */
    private void concatDelayError() {
        //如果用concat，被观察者2是没机会发出7事件的。
        Observable.concatArrayDelayError(Observable.create(new ObservableOnSubscribe<Integer>() {

                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onError(new NullPointerException());
                        // 发送Error事件，因为使用了concatDelayError，所以第2个Observable将会发送事件，等发送完毕后，再发送错误事件
                        emitter.onComplete();
                    }
                }),
                Observable.just(4, 5, 6, 7))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * zip（）
     * 作用:
     * 合并 多个被观察者（Observable）发送的事件，生成一个新的事件序列（即组合过后的事件序列）
     * ，最终合并的事件数量 = 多个观察者中数量最少的数量
     *
     * 打印结果：
     * 1A、2B、3C 就结束了，observable2的D事件没接收
     */
    private void zip() {
        //第一个观察者
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                KLog.d(TAG, "Observable1 emitter 1");
                emitter.onNext(1);
                KLog.d(TAG, "Observable1 emitter 2");
                emitter.onNext(2);
                KLog.d(TAG, "Observable1 emitter 3");
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                KLog.d(TAG, "Observable2 emitter A");
                emitter.onNext("A");
                KLog.d(TAG, "Observable2 emitter B");
                emitter.onNext("B");
                KLog.d(TAG, "Observable2 emitter C");
                emitter.onNext("C");
                KLog.d(TAG, "Observable2 emitter D");
                emitter.onNext("D");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());
        //假设不作线程控制，则该两个被观察者会在同一个线程中工作，即发送事件存在先后顺序，而不是同时发送

        //创建BiFunction对象传入的第3个参数 = 合并后数据的数据类型
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                KLog.d(TAG, "最终接收到的事件 =  " + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                KLog.d(TAG, "onComplete");
            }
        });
    }

    /**
     * combineLatest()
     * 作用:
     * 当两个Observables中的任何一个发送了数据后，将先发送了数据的Observables 的最新（最后）一个数据
     * 与 另外一个Observable发送的每个数据结合，最终基于该函数的结果发送数据
     * <p>
     * 与zip（）的区别：zip（） = 按个数合并，即1对1合并；CombineLatest（） = 按时间合并，即在同一个时间点上合并
     *
     */
    private void combineLatest() {
        Observable.combineLatest(Observable.just(1, 2, 3),
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS),
                new BiFunction<Integer, Long, Long>() {
                    @Override
                    public Long apply(Integer integer, Long aLong) throws Exception {
                        // integer = 第1个Observable发送的最新（最后）1个数据
                        // aLong = 第2个Observable发送的每1个数据
                        KLog.e(TAG, "合并的数据是： " + integer + " " + aLong);
                        return integer + aLong;
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                KLog.e(TAG, "合并的结果是： " + aLong);
            }
        });
    }


    /**
     * reduce()
     * 作用：把被观察者需要发送的事件缩减成1个事件 & 发送
     *
     * 打印结果：
     * 1*2*3*4 = 24
     */
    private void reduce() {
        Observable.just(1, 2, 3, 4)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        KLog.e(TAG, "本次计算的数据是： " + integer + " 乘 " + integer2);
                        return integer * integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                KLog.e(TAG, "最终计算的结果是： " + integer);
                //此例子，结果为24。
            }
        });
    }

    /**
     * collect()
     * 作用
     * 将被观察者Observable发送的数据事件收集到一个数据结构里
     */
    private void collect() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .collect(new Callable<ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> call() throws Exception {
                        return new ArrayList<>();
                    }
                }, new BiConsumer<ArrayList<Integer>, Integer>() {
                    @Override
                    public void accept(ArrayList<Integer> list, Integer integer) throws Exception {
                        // 参数说明：list = 容器，integer = 后者数据
                        list.add(integer);
                        // 对发送的数据进行收集
                    }
                }).subscribe(new Consumer<ArrayList<Integer>>() {
            @Override
            public void accept(ArrayList<Integer> s) throws Exception {
                KLog.e(TAG, "本次发送的数据是： " + s);
            }
        });
    }


    /**
     * startWith（） / startWithArray（）
     * 发送事件前追加发送事件
     * <p>
     * count（）统计被观察者发送事件的数量
     */
    private void startWith() {
        //startWith的先被调用
        Observable.just(4, 5, 6)
                .startWith(0)
                .startWithArray(1, 2, 3)
                .startWith(Observable.just(7, 8, 9))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        KLog.d(TAG, "对Complete事件作出响应");
                    }
                });
    }


}
