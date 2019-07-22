package com.sz.dzh.drxjavasummary.module.rxUse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;
import com.sz.dzh.drxjavasummary.bean.NetBean;
import com.sz.dzh.drxjavasummary.bean.OrderBean;
import com.sz.dzh.drxjavasummary.bean.RxEventBean;
import com.sz.dzh.drxjavasummary.utils.RxBus;
import com.sz.dzh.drxjavasummary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dengzh on 2019/7/21 0021.
 */
public class RxBusBActvitiy extends BaseActivity {

    @BindView(R.id.tv_desc)
    TextView tvDesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rx_bus2);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_send, R.id.btn_send2, R.id.btn_send3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                RxBus.getIntanceBus().post("这是String类型的事件");
                break;
            case R.id.btn_send2:
                RxEventBean<NetBean> rxEventBean = new RxEventBean<>();
                rxEventBean.setCode(404);
                rxEventBean.setContent(new NetBean("111","这是NetBean"));
                RxBus.getIntanceBus().post(rxEventBean);
                break;
            case R.id.btn_send3:
                RxBus.getIntanceBus().post(100,new OrderBean(1,"冬瓜",10,"深圳"));
                break;
        }
    }
}
