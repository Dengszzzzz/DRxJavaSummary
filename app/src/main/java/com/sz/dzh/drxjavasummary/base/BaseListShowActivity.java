package com.sz.dzh.drxjavasummary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.bean.ClazzBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dengzh on 2018/4/18.
 * 列表显示界面基类
 */

public abstract class BaseListShowActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    protected List<ClazzBean> mList = new ArrayList<>();
    protected BaseListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_custom_list);
        ButterKnife.bind(this);
        initTitle();
        initView();
        initUI();
        initData();
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


    protected abstract void initUI();
    //子类给mList添加数据，再调用mAdapter.notifyDataSetChanged();
    protected abstract void initData();

    protected void addClazzBean(String name, Class clazz){
        mList.add(new ClazzBean(name,clazz));
    }


}
