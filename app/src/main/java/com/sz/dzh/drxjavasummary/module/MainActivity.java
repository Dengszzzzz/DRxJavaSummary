package com.sz.dzh.drxjavasummary.module;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.base.BaseActivity;
import com.sz.dzh.drxjavasummary.base.BaseListAdapter;
import com.sz.dzh.drxjavasummary.base.BaseListShowActivity;
import com.sz.dzh.drxjavasummary.bean.ClazzBean;
import com.sz.dzh.drxjavasummary.module.rxStudy.RxJStudyListActivity;
import com.sz.dzh.drxjavasummary.module.rxUse.RxUseListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by administrator on 2018/11/5.
 * 1.RxJava是什么，它优势是什么？
 * 2.RxJava2基础知识。
 * 3.RxJava2操作符。
 * 4.背压策略。
 * 5.具体的使用场景。
 *
 *
 * 参考：
 * 教程：
 * 给 Android 开发者的 RxJava 详解
 * http://gank.io/post/560e15be2dca930e00da1083
 * 这可能是最好的RxJava 2.x 入门教程
 * https://www.jianshu.com/p/a93c79e9f689
 * Android拾萃 - RxJava2操作符汇总
 * https://www.jianshu.com/p/f54f32b39b7c
 * Rxjava2入门教程五：Flowable背压支持——对Flowable最全面而详细的讲解
 * https://www.jianshu.com/p/ff8167c1d191/
 * Android Rxjava：这是一篇 清晰 & 易懂的Rxjava 入门教程
 * https://www.jianshu.com/p/a406b94f3188
 *
 *
 * 实际应用场景：
 * RxBus
 * https://www.jianshu.com/p/3a3462535b4d
 * RxLifecycle原理分析
 * https://www.jianshu.com/p/e75d320a668c
 * https://www.jianshu.com/p/0d07fba84cb8
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    protected List<ClazzBean> mList = new ArrayList<>();
    protected BaseListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTitle();
        ivBack.setVisibility(View.GONE);
        tvTitle.setText("RxJava2学习笔记");
        initData();
        initView();
    }

    private void initData(){
        addClazzBean("RxJava2 基础知识", RxJStudyListActivity.class);
        addClazzBean("RxJava2 实际应用场景", RxUseListActivity.class);
        addClazzBean("RxJava2 实际应用场景2", RxUseListActivity.class);
    }

    private void initView(){
        mAdapter = new BaseListAdapter(mList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(mList.get(position).getClazz());
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }



    protected void addClazzBean(String name, Class clazz){
        mList.add(new ClazzBean(name,clazz));
    }

}
