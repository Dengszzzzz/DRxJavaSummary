package com.sz.dzh.drxjavasummary.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.socks.library.KLog;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.utils.AppManager;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import java.lang.reflect.ParameterizedType;

/**
 * base
 */
public abstract class BaseActivity<V extends BaseView, P extends BasePresenterImpl<V>> extends RxFragmentActivity implements BaseView {

    protected String TAG = getClass().getSimpleName();

    //标题栏
    protected RelativeLayout titleBar;
    protected ImageView ivBack,ivRight;
    protected TextView tvBack,tvTitle,tvRight;

    public P mPresenter;
    KProgressHUD kProgressHUD;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppManager.getAppManager().addActivity(this);
        printRunningActivity(this, true);

        kProgressHUD = new KProgressHUD(this);
        mPresenter = getInstance(this, 1);
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }

    }

    /**
     * 初始化标题
     */
    protected void initTitle(){
        titleBar = findViewById(R.id.titleBar);
        ivBack =  findViewById(R.id.ivBack);
        tvBack = findViewById(R.id.tvBack);
        tvTitle =  findViewById(R.id.tvTitle);
        ivRight =  findViewById(R.id.ivRight);
        tvRight =  findViewById(R.id.tvRight);
        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        printRunningActivity(this, false);
        AppManager.getAppManager().finishActivity(this);
        if (mPresenter != null){
            mPresenter.detachView();
            mPresenter = null;
        }
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
            kProgressHUD = null;
        }
    }

    //反射获取当前Presenter对象
    public <M> M getInstance(Object o, int i) {
        try {
            return ((Class<M>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (kProgressHUD.isShowing()) {
                    return;
                }
                kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .setCancellable(true)
                        .show();
            }
        });
    }

    @Override
    public void dismissLoading() {
        if (kProgressHUD != null && kProgressHUD.isShowing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    kProgressHUD.dismiss();
                }
            });
        }
    }

    /**
     * 跳转 减少重复代码
     * @param tarActivity 目标activity
     */
    public void startActivity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }


    /**
     * 打印activity信息
     *
     * @param ac
     * @param isRunning
     */
    protected void printRunningActivity(Activity ac, boolean isRunning) {
        String contextString = ac.toString();
        String s = contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
        if (isRunning) {
            KLog.e("Activity", "app:当前正在加入的界面是:" + s);
        } else {
            KLog.e("Activity", "app:当前销毁的界面是:" + s);
        }
    }


}
