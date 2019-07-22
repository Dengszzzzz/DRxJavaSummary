package com.sz.dzh.drxjavasummary.module.rxlifecycle;

import com.sz.dzh.drxjavasummary.base.BasePresenter;
import com.sz.dzh.drxjavasummary.base.BaseView;

/**
 * @author
 * @date 2018/7/25
 * @description
 */
public class RxlifecContract {

     public interface View extends BaseView {
         void onTest1Success(String str);
         void onTest2Success(String str);
     }

     public interface  Presenter extends BasePresenter<View> {
         void onTest1() ;
         void onTest2() ;
    }


}
