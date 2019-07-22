package com.sz.dzh.drxjavasummary.module.rxUse;


import com.sz.dzh.drxjavasummary.base.BaseListShowActivity;

/**
 * Created by administrator on 2018/11/5.
 * RxJava2 实际应用场景
 * 1.RxBus 替换 EventBus
 */
public class RxUseListActivity extends BaseListShowActivity {
    @Override
    protected void initUI() {
        tvTitle.setText("RxJava2 实际应用场景");
    }

    @Override
    protected void initData() {
        addClazzBean("RxBus使用", RxBusActvitiy.class);
        addClazzBean("验证码倒计时", RxCodeActvitiy.class);
        addClazzBean("RxLifecycle使用", RxLifecycleActivity.class);
    }


    /**
     * Subject
     *   Subject 可以同时代表 Observer 和 Observable，允许从数据源中多次发送结果给多个观察者。
     *
     * 1、AsyncSubject
     *    只有当 Subject 调用 onComplete 方法时，才会将 Subject 中的最后一个事件传递给所有的 Observer。
     *
     * 2.BehaviorSubject
     *    该类有创建时需要一个默认参数，该默认参数会在 Subject 未发送过其他的事件时，向注册的 Observer 发送；
     *    新注册的 Observer 不会收到之前发送的事件，这点和 PublishSubject 一致。
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
}
