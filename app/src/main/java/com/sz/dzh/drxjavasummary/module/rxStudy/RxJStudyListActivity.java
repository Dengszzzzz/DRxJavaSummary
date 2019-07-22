package com.sz.dzh.drxjavasummary.module.rxStudy;

import com.sz.dzh.drxjavasummary.base.BaseListShowActivity;

/**
 * Created by administrator on 2018/11/5.
 */
public class RxJStudyListActivity extends BaseListShowActivity {
    @Override
    protected void initUI() {
        tvTitle.setText("RxJava2 基础知识");
    }

    @Override
    protected void initData() {
        addClazzBean("创建操作符", RxCreateActivity.class);
        addClazzBean("转换操作符", RxTransformActivity.class);
        addClazzBean("组合操作符", RxCombineActivity.class);
        addClazzBean("功能操作符", RxFunctionActivity.class);
        addClazzBean("过滤操作符", RxFilterActivity.class);
        addClazzBean("条件操作符", RxConditionActivity.class);
        addClazzBean("背压策略", FlowableActivity.class);
    }

}
