package com.sz.dzh.drxjavasummary.module.rxStudy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;
import com.sz.dzh.drxjavasummary.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by administrator on 2018/11/8.
 * 功能性操作符
 * 1.subscribe（）   订阅，即连接观察者 & 被观察者
 * 2.delay（）       使得被观察者延迟一段时间再发送事件
 * 3.do()            各个事件操作符
 * 4.retry()         重试，即当出现错误时，让被观察者（Observable）重新发射数据
 * retryUntil（）     Observable遇到错误时，是否让Observable重新订阅
 * retryWhen（）       retryWhen将onError中的Throwable传递给一个函数，这个函数产生另一个Observable，由这个Observable来决定是否要重新订阅原Observable。
 * 5.repeat（）                  无条件地、重复发送 被观察者事件
 * repeatWhen（Integer int）   传入参数 = 重复发送次数有限
 * <p>
 * <p>
 * 此类功能符应用场景：
 * 1.线程操作（切换 / 调度 / 控制 ）
 * 2.轮询
 * 3.发送网络请求时的差错重试机制
 */
public class RxFunctionActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_function);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("功能性操作符");
    }

    @OnClick({R.id.btn_do, R.id.btn_retry, R.id.btn_retryUntil, R.id.btn_retryWhen, R.id.btn_repeat, R.id.btn_repeatWhen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_do:
                doRx();
                break;
            case R.id.btn_retry:
                retry();
                break;
            case R.id.btn_retryUntil:
                ToastUtils.showToast("Observable遇到错误时，是否让Observable重新订阅,和retry()类似");
                break;
            case R.id.btn_retryWhen:
                retryWhen();
                break;
            case R.id.btn_repeat:
                repeat();
                break;
            case R.id.btn_repeatWhen:
                repeatWhen();
                break;
        }
    }


    private void doRx() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new Throwable("发生错误了"));
            }
        })
                //1.当Observable每发送1次数据事件就会调用1次
                .doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        Log.d(TAG, "doOnEach:----------------- " + integerNotification.getValue());
                    }
                })
                // 2. 执行Next事件前调用
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext: " + integer);
                    }
                })
                // 3. 执行Next事件后调用
                .doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doAfterNext: " + integer);
                    }
                })
                // 4. Observable正常发送事件完毕后调用
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "doOnComplete: ");
                    }
                })
                // 5. Observable发送错误事件时调用
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "doOnError: " + throwable.getMessage());
                    }
                })
                // 6. 观察者订阅时调用
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.d(TAG, "doOnSubscribe: ");
                    }
                })
                // 7. Observable发送事件完毕后调用，无论正常发送完毕 / 异常终止
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "doAfterTerminate: ");
                    }
                })
                // 8. 最后执行
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "doFinally: ");
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "对Complete事件作出响应");
                    }
                });

    }

    /**
     * retry()
     * 重试
     *
     */
    private void retry(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                for(int i = 0; i<= 3 ;i++){
                    if(i == 2){
                        emitter.onError(new Exception("出现错误了"));
                    }else{
                        emitter.onNext(i+"");
                    }
                    try{
                        Thread.sleep(1000);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .retry(3, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Exception {
                        KLog.e(TAG, "retry错误: "+throwable.toString());
                        //最多让被观察者重新发射数据3次，但是这里返回值可以进行处理
                        //返回false 不再重试，调用观察者的onError就终止了。
                        //返回true 重试
                        return true;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "接收事件: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError(): " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }


    /**
     * retryWhen()
     * 和retry()类似。
     * 区别是: retryWhen将onError中的Throwable传递给一个函数，这个函数产生另一个Observable，
     * 由这个Observable来决定是否要重新订阅原Observable。
     *
     */
    private void retryWhen(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                for(int i = 0; i<= 3 ;i++){
                    if(i == 2){
                        emitter.onError(new Exception("500"));
                    }else{
                        emitter.onNext(i+"");
                    }
                    try{
                        Thread.sleep(1000);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        //这里可以发送新的被观察者 Observable

                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if(throwable.getMessage().equals("500")){
                                    //如果是500，延迟3s再重发
                                    return Observable.timer(3, TimeUnit.SECONDS);
                                }
                                //如果发射的onError就终止
                                return Observable.error(new Throwable("retryWhen终止啦"));
                            }
                        });
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "接收事件: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError(): " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }


    /**
     * repeat()
     * Observable产生重复事件，且发送了onComplete()，repeat才生效
     *
     * 打印结果：
     * 重复了2遍，且只有最后一次有onComplete()打印
     */
    private void repeat(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                KLog.d(TAG, "Observable emitter 1" + "\n");
                emitter.onNext(1);
                KLog.d(TAG, "Observable emitter 2" + "\n");
                emitter.onNext(2);
                KLog.d(TAG, "Observable emitter 3" + "\n");
                emitter.onNext(3);
                emitter.onComplete();  //必须写，repeat的必要条件
            }
        }).repeat(2)  //产生两个重复事件
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "接收事件" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        KLog.d(TAG, "onComplete()");
                    }
                });
    }


    /**
     * repeatWhen()
     * 加了判断条件，看是否需要重复
     * repeatWhen里的ObservableSource，如果发送onNext会重发，发送onComplete/onError不会重发。
     * 注意：
     * 如果发送onComplete，Observer不会回调onComplete
     * 如果发送onError，Observer会回调onError
     *
     * 打印结果：
     * 重复了3遍，且onComplete()不打印
     */
    private int n;
    private void repeatWhen(){
        n = 0;
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onComplete();  //必须写，repeat的必要条件
            }
        }).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        if (n != 2) {
                            n++;
                            return Observable.timer(1, TimeUnit.SECONDS);
                        } else {
                            return Observable.empty(); //empty() 只会发送onComplete()事件
                        }
                    }
                });
            }
        })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        KLog.d(TAG, "接收事件" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        KLog.d(TAG, "onComplete()");
                    }
                });
    }

}
