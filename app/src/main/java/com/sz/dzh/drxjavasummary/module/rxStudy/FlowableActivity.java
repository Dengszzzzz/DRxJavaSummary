package com.sz.dzh.drxjavasummary.module.rxStudy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by administrator on 2018/11/9.
 *
 * 背压问题：
 *     当在异步订阅中，通过Observable发射、处理、响应数据流时，如果事件产生的速度远远快于事件消费的速度，这些没来得及
 * 处理的数据就会越积越多，这些数据不会丢失，也不会被垃圾回收机制回收，而是存放在一个异步缓存池中，缓存池的数据一直
 * 得不到处理，最终会导致OOM等异常。这就是响应式编程中的背压问题。总结一下就是： 事件产生的速度大于事件消费的速度，
 * 数据堆积，最终造成OOM等异常。
 *     RxJava2把对背压问题的处理逻辑从Observable中抽取出来产生了新的可观察对象Flowable，它是在Observable基础
 * 上做了优化，所以Observable能做的，它都能做，但是加了背压支持和其他的逻辑处理，它的效率比Observable慢得多，所
 * 以在需要用到背压的时候再用Flowable，其他时候还是正常使用Observable。
 */
public class FlowableActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_flowable);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("背压问题");
    }

    @OnClick({R.id.btn_asynchronous, R.id.btn_synchronized})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_asynchronous:
                asynchronizedTest();
                break;
            case R.id.btn_synchronized:
                synchronizedTest();
                break;
        }
    }

    /**
     * Flowable 和 Observable的不同点：
     * 1、create方法中多了一个BackpressureStrategy类型的参数。
     * 2、Flowable发射数据时，使用特有的发射器FlowableEmitter，不同于Observable的ObservableEmitter。
     * 3、Subscriber中，方法onSubscribe回调的参数不是Disposable而是Subscription。
     * */

    /**
     * Flowable与Observable在功能上的区别主要是 多了背压的功能
     * 异步订阅情况举例
     * 1.上流发送很多事件，放入缓存区
     * 2.下流 指定从缓存区 获取多少个事件  request()
     *
     * 打印结果：
     * request(3),接收3个事件，所以打印出 ———— 接收到了事件1、...2、...3
     */
    private void asynchronizedTest() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                //异步订阅时，代表的是 异步缓存池中可放入数据的数量，一开始是128，当产生10个事件而没有消费时，此时这个值是128-10=118。
                KLog.d(TAG, "异步缓存池中可放入数据的数量 = " + emitter.requested());

                // 一共发送4个事件
                KLog.d(TAG, "发送事件 1");
                emitter.onNext(1);
                KLog.d(TAG, "发送事件 2");
                emitter.onNext(2);
                KLog.d(TAG, "发送事件 3");
                emitter.onNext(3);
                KLog.d(TAG, "发送事件 4");
                emitter.onNext(4);
                KLog.d(TAG, "发送事件 onComplete()");
                emitter.onComplete();

                KLog.d(TAG, "异步缓存池中可放入数据的数量 = " + emitter.requested());

//                //模拟缓存超过128
//                for (int i = 0;i< 129; i++) {
//                    Log.d(TAG, "发送了事件" + i);
//                    emitter.onNext(i);
//                }
//                emitter.onComplete();
//
            }
        }, BackpressureStrategy.ERROR)  //缓存区超过128，直接抛出异常
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        // 在异步订阅情况下，一定要调用request，否则下流不接收事件
                        // 只接收多少个事件
                        s.request(3);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        KLog.d(TAG, "onError：", t);
                    }

                    @Override
                    public void onComplete() {
                        KLog.d(TAG, "onComplete");
                    }
                });
    }


    /**
     * 同步订阅：
     * 不存在缓存区，request用于控制流速，下流需要多少，上流才发送多少
     */
    private void synchronizedTest() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                long requestCount = emitter.requested();
                //同步订阅时，代表的是下游需要的事件数量
                KLog.d(TAG, "下游可接收事件数量 = " + requestCount);

                // 根据emitter.requested()的值，即当前观察者需要接收的事件数量来发送事件
                for (int i = 0; i < requestCount; i++) {
                    Log.d(TAG, "发送了事件" + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //Subscription.request(n) 可以累加
                        s.request(3);
                        s.request(7);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        KLog.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        KLog.d(TAG, "onComplete");
                    }
                });


    }




    /***
     * 背压模式
     * 面向对象：针对缓存区
     * 作用：当缓存区大小存满、被观察者仍然继续发送下1个事件时，该如何处理的策略方式
     * 缓存区大小存满、溢出 = 发送事件速度 ＞ 接收事件速度 的结果 = 发送 & 接收事件不匹配的结果
     *
     * BackpressureStrategy.ERROR;     直接抛异常
     * BackpressureStrategy.MISSING;   友好提示：缓存区满了
     * BackpressureStrategy.BUFFER;    将缓存区设为无限大
     * BackpressureStrategy.DROP;      超过缓存区大小(128)的事件丢弃
     * BackpressureStrategy.LATEST;    只保存最后事件，超过缓存区大小(128)的事件丢弃
     *
     * 对于非自身手动创建的Flowable，可以用 onBackpressureXXX()方法
     *  Flowable.interval(1, TimeUnit.MILLISECONDS).onBackpressureBuffer()
     *
     *  也可以使用toFlowable(BackpressureStrategy.XXX) 操作符 添加背压模式
     * **/
}
