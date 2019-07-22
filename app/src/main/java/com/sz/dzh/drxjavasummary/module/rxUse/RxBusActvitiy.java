package com.sz.dzh.drxjavasummary.module.rxUse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;
import com.sz.dzh.drxjavasummary.bean.NetBean;
import com.sz.dzh.drxjavasummary.bean.OrderBean;
import com.sz.dzh.drxjavasummary.utils.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dengzh on 2019/7/21 0021.
 * RxBus
 */
public class RxBusActvitiy extends BaseActivity {

    @BindView(R.id.tv_desc)
    TextView tvDesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_bus);
        ButterKnife.bind(this);
        testRxBus();
        testRxBus2();
        testRxBus3();
    }

    /**
     * 使用RxBus里提供的doSubscribe()
     * 对线程进行了封装，没有用链式结构
     */
    private void testRxBus(){
        Disposable disposable = RxBus.getIntanceBus().doSubscribe(String.class, s -> {
            KLog.d(TAG,"testRxBus() 接收到 String.class 的事件");
            KLog.d(TAG,"testRxBus() onNext()：" + s);
        }, throwable -> KLog.d(TAG,"onError()：" + throwable.toString()));
        //保存disposable
        RxBus.getIntanceBus().addSubscription(this,disposable);
    }

    /**
     * 和testRxBus一样的效果，此处用了链式
     * 而且使用EventBus很少处理异常，这里也不进行异常处理了
     */
    private void testRxBus2(){
        Disposable disposable = RxBus.getIntanceBus()
                .getObservable(String.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        KLog.d(TAG,"testRxBus2() 接收到 String.class 的事件");
                        KLog.d(TAG,"testRxBus2() onNext()：" + s);
                    }
                });
        RxBus.getIntanceBus().addSubscription(this,disposable);
    }


    private void testRxBus3(){
        RxBus.getIntanceBus().register(this, rxEvents -> {
            KLog.d(TAG,"testRxBus3() 接收到 RxEventBean.class 的事件");
            //用code判断，再强转Object
            if(rxEvents.getCode()==404){
                NetBean netBean = (NetBean) rxEvents.getContent();
                KLog.d(TAG,"content:" + netBean.toString());
            }else if(rxEvents.getCode() == 100){
                OrderBean orderBean = (OrderBean) rxEvents.getContent();
                KLog.d(TAG,"content:" + orderBean.toString());
            }});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        RxBus.getIntanceBus().unSubscribe(this);
    }

    @OnClick(R.id.btn_into)
    public void onViewClicked() {
        startActivity(RxBusBActvitiy.class);
    }
}
