package com.sz.dzh.drxjavasummary.base;



public interface BasePresenter<V extends BaseView>{
    void attachView(V view);

    void detachView();
}
