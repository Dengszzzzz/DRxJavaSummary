package com.sz.dzh.drxjavasummary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by dengzh on 2018/4/18.
 * 1.传递参数，通过setArguments(Bundle bundle)方式，而不是通过Fragment添加带参数的构造函数。
 *   好处是：在由于内存紧张导致Fragment被系统杀掉并恢复（re-instantiate）时能保留这些数据。
 * 2.生命周期方法，只有onCreateView()在重写时不用写super方法，其他都需要
 * 3.了解Back Stack(回退栈)的使用和原理
 * 4.常用的getSupportFragmentManager().findFragmentByTag()
 *
 *
 * fragment的好处？
 * 1）碎片、片段。其目的是为了解决不同屏幕分辩率的动态和灵活UI设计。
 * 2）将activity分离成多个可重用的组件，每个都有它自己的生命周期和UI。
 * 3）灵活的UI设计，可以适应于不同的屏幕尺寸。
 * 4）是一个独立的模块,紧紧地与activity绑定在一起。可以运行中动态地移除、加入、交换等。
 * 5）切换流畅，轻量切换，替代TabActivity做导航，性能更好。
 */

public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    /**
     * Fragment和Activity相关联时调用。可以通过该方法获取Activity引用，还可以通过getArguments()获取参数
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    /**
     * Fragment被创建时调用
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 创建Fragment的布局  在创建Fragment调用这个方法布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //第三个参数是false，因为在Fragment内部实现中，会把该布局添加到container中，如果设为true，那么就会重复做两次添加，则会抛如下异常：
        //Caused by: java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first
        View view = inflater.inflate(getLayoutId(), container,false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }*/

    /**
     * 当Activity完成onCreate()时调用
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 当Fragment可见时调用
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 当Fragment可见且可交互时调用
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 当Fragment不可交互但可见时调用
     */
    @Override
    public void onPause() {
        super.onPause();
    }


    /**
     * 当Fragment不可见时调用
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 当Fragment的UI从视图结构中移除时调用
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 销毁Fragment时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 当Fragment和Activity解除关联时调用
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * 跳转 减少重复代码
     * @param tarActivity 目标activity
     */
    public void startActivity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(mActivity, tarActivity);
        startActivity(intent);
    }
}
