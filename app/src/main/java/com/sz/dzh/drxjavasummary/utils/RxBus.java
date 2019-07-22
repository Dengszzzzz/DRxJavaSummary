package com.sz.dzh.drxjavasummary.utils;

import android.content.Context;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.bean.RxEventBean;

import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by dengzh on 2017/9/21 0021.
 * EventBus是一个基于发布/订阅的事件总线，它简化了组件之间的通信操作,且有黏性事件。
 * RxBus基于上述功能进行开发。
 * 原理：
 *     找个容器装所有的观察者，当有某个事件产生时，找到所有需要这个事件的观察者，向它们发送事件。
 *
 * 相关知识：
 * 1）CompositeDisposable
 *    一个disposable的容器，可以容纳多个disposable
 * 2）Subject
 *    Subject可以同时代表 Observer 和 Observable，允许从数据源中多次发送结果给多个观察者。
 *
 *
 * 源码来自：
 * https://www.jianshu.com/p/3a3462535b4d
 */

public class RxBus {

    /**
     * CompositeDisposable定义：
     * 一个disposable的容器，可以容纳多个disposable，添加和去除的复杂度为O(1)
     *
     * 此处使用目的：
     * 是为了一个订阅者能够对应多个Disposable，在需要的时候调用 Disposable 的 dispose()取消订阅。
     *
     * 举个例子：
     * XXActivity，订阅了事件A和事件B，关闭时要取消订阅。
     * 那么只需在mSubscriptionMap里找到key为XXActivity的Value（CompositeDisposable），再取出Disposable
     * 取消订阅即可。
     *
     * 如果不用的话，就要为每一个Activity写一个容器去保存它的订阅的事件了，相当麻烦。
     * */
    private HashMap<String, CompositeDisposable> mSubscriptionMap;
    private static volatile RxBus mRxBus;
    private final Subject<Object> mSubject;

    public static RxBus getIntanceBus(){
        if (mRxBus==null){
            synchronized (RxBus.class){
                if(mRxBus==null){
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }


    /**
     * Subject定义：
     *   Subject 可以同时代表 Observer 和 Observable，允许从数据源中多次发送结果给多个观察者。
     *
     * 1、AsyncSubject
     *    只有当 Subject 调用 onComplete 方法时，才会将 Subject 中的最后一个事件传递给所有的 Observer。
     *
     * 2.BehaviorSubject
     *    当观察者订阅BehaviorSubject时，它开始发射原始Observable最近发射的数据（如果此时还没有收到任何数据，
     *    它会发射一个默认值），然后继续发射其它任何来自原始Observable的数据。然而，如果原始的Observable因为
     *    发生了一个错误而终止，BehaviorSubject将不会发射任何数据，只是简单的向前传递这个错误通知。
     *    Rxlifecycle2（RxJava绑定声明周期的库）中用到。
     *
     * 3.PublishSubject
     *    不会改变事件的发送顺序；在已经发送了一部分事件之后注册的 Observer 不会收到之前发送的事件。
     *
     * 4.ReplaySubject
     *    无论什么时候注册 Observer 都可以接收到任何时候通过该 Observable 发射的事件。
     *
     * 5.UnicastSubject
     *   只允许一个 Observer 进行监听，在该 Observer 注册之前会将发射的所有的事件放进一个队列中，
     *   并在 Observer 注册的时候一起通知给它。
     * */

    /**
     * 正常订阅可用PublishSubject、黏性事件可用ReplaySubject。
     */
    private RxBus(){
        mSubject = PublishSubject.create().toSerialized();
    }

    /**
     * 一个默认的订阅方法
     * @param <T>
     * @param type   过滤，只返回特定数据类型的数据
     * @param next   next()事件,正常接收的事件
     * @param error  error()事件，错误接收的事件
     * @return
     */
    public <T> Disposable doSubscribe(Class<T> type, Consumer<T> next, Consumer<Throwable> error){
        return getObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next,error);
    }

    /**
     * 返回指定类型的带背压的Flowable实例
     * 这里选定背压模式是 BackpressureStrategy.BUFFER，缓存区无限大
     * @param <T>
     * @param type  过滤，只返回特定数据类型的数据
     * @return
     */
    public <T> Flowable<T> getObservable(Class<T> type){
        return mSubject.toFlowable(BackpressureStrategy.BUFFER).ofType(type);
    }

    /**
     * 发送事件
     * @param o 事件
     */
    public void post(Object o){
        mSubject.onNext(o);
    }


    /**
     * 判断是否已有观察者订阅
     *
     * @return
     */
    public boolean hasObservers() {
        return mSubject.hasObservers();
    }

    /**
     * 保存订阅后的disposable，取消订阅的时候要用
     * @param o       订阅的目标
     * @param disposable
     */
    public void addSubscription(Object o, Disposable disposable) {
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        //这里key值取订阅目标的实体名称(com.xx.xx.xxx)，不是底层类检称
        String key = o.getClass().getName();
        //disposable 放到对应的 CompositeDisposable 里
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(disposable);
        } else {
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            mSubscriptionMap.put(key, disposables);
        }
    }

    /**
     * 取消订阅
     * @param o
     */
    public void unSubscribe(Object o) {
        if (mSubscriptionMap == null) {
            return;
        }
        String key = o.getClass().getName();
        if (!mSubscriptionMap.containsKey(key)){
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).dispose();
        }
        mSubscriptionMap.remove(key);
    }

    /********************************* 为RxEventBean 封装  **********************************/
    /**
     * 使用EventBus时，为了方便查找，一般都会封装 EventBean(int code,Object content)
     * 但是所有的订阅者都是订阅的这个类型，所以要自己做判断做类型转换。
     *
     * 这里写死事件是RxEventBean，且错误处理一般不处理,只用处理onNext即可。
     * @param context
     * @param action
     * */
    public void register(Context context, Consumer<RxEventBean> action) {
        Disposable disposable = RxBus.getIntanceBus().doSubscribe(RxEventBean.class, action,
                throwable -> KLog.e("RxEventBean onError()", throwable.toString()));
        RxBus.getIntanceBus().addSubscription(context,disposable);
    }


    /**
     * 发送RxEventBean 事件
     * @param code     code，用于判断
     * @param content  内容，接收后做类型转换
     */
    public void post(int code, Object content){
        RxEventBean<Object> event = new RxEventBean<>();
        event.code = code;
        event.content = content;
        post(event);
    }
}
