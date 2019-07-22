package com.sz.dzh.drxjavasummary.module.rxUse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;
import com.sz.dzh.drxjavasummary.module.rxlifecycle.RxlifePresenter;
import com.sz.dzh.drxjavasummary.module.rxlifecycle.RxlifecContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dengzh
 * on 2019/7/22 0022
 * RxLifecycle
 * 1.目的：解决RxJava使用中的内存泄漏问题
 * <p>
 * 2.绑定生命周期：
 * bindUntilEvent(@NonNull ActivityEvent event)   绑定某个生命周期
 * bindToLifecycle()
 * 绑定生命周期，取消订阅策略可看源码。以Activity为例，可看RxLifecycleAndroid 下的 ACTIVITY_LIFECYCLE，
 * 在onCreate()订阅的，在onDESTROY()取消订阅；在onResume()订阅的，在onPause()取消订阅。
 * 所以做网络请求时无特殊要求用bindToLifecycle()即可，如果要在onPause()不取消订阅，就用bindUntilEvent(xxx)。
 * <p>
 * 3.相关知识：
 * compose()：
 * 将一种类型的Observable转换成另一种类型的Observable，保证调用的链式结构。
 * BehaviorSubject：
 * 发送离订阅最近的上一个值，没有上一个值的时候会发送默认值。比如我们在界面点击按钮发起请求，这时subject会发送最近的生命周期给这个事件。
 * LifecycleTransformer：
 * LifecycleTransformer实现了各种Transformer接口，能够将一个Observable/Flowable/Single/Completable/Maybe
 * 对象转换成另一个 Observable/Flowable/Single/Completable/Maybe对象。正好配合上文的compose操作符，使用在链式调用中。
 * takeUntil(Observable2)：
 * 执行，直到xx时停止。也就是Observable2发送一个事件，Observable1就取消订阅。
 * take(n)：
 * 只发射前面n个事件。
 * skip(n):
 * 跳过前面n个事件发射。
 * combineLatest():
 * 将多个Observable发射的数据组装起来再发射。
 * <p>
 * 参考：
 * https://www.jianshu.com/p/e75d320a668c
 * https://www.jianshu.com/p/0d07fba84cb8
 */
public class RxLifecycleActivity extends BaseActivity<RxlifecContract.View, RxlifePresenter> implements RxlifecContract.View {

    @BindView(R.id.tv_desc1)
    TextView mTvDesc1;
    @BindView(R.id.tv_desc2)
    TextView mTvDesc2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_life_cycle);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1, R.id.btn2,R.id.btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                mTvDesc1.setText("");
                mPresenter.onTest1();
                break;
            case R.id.btn2:
                mTvDesc2.setText("");
                mPresenter.onTest2();
                break;
            case R.id.btn3: //进入下一个页面，验证它们是否取消了订阅
                startActivity(RxBusActvitiy.class);
                break;
        }
    }


    @Override
    public void onTest1Success(String str) {
        mTvDesc1.setText(str);
    }

    @Override
    public void onTest2Success(String str) {
        mTvDesc2.setText(str);
    }
}
