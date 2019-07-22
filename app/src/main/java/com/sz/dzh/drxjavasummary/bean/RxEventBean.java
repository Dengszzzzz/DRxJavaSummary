package com.sz.dzh.drxjavasummary.bean;

/**
 * Created by dengzh on 2017/9/21 0021.
 * rxBus 的事件
 */

public class RxEventBean<T> {

    public int code;
    public T content;

    public RxEventBean() {
    }

    public RxEventBean(int code, T content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }



    //    public static <O> RxEvents<O> setContent(O t) {
//        RxEvents<O> events = new RxEvents<>();
//        events.content = t;
//        return events;
//    }
//
//    public <T> T getContent() {
//        return (T) content;
//    }

}
