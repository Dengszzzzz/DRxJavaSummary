package com.sz.dzh.drxjavasummary.base;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sz.dzh.drxjavasummary.R;
import com.sz.dzh.drxjavasummary.bean.ClazzBean;

import java.util.List;


/**
 * Created by dengzh on 2018/4/18.
 */

public class BaseListAdapter extends BaseQuickAdapter<ClazzBean, BaseViewHolder> {

    public BaseListAdapter(@Nullable List<ClazzBean> data) {
        super(R.layout.item_base_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClazzBean item) {
        helper.setText(R.id.tvName,item.getClazzName());
    }
}
