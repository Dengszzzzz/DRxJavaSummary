package com.sz.dzh.drxjavasummary.bean;

/**
 * Created by Dengzh
 * on 2019/7/18 0018
 * RxJava 操作符
 */
public class RxOperatorBean {
    private int id;
    private String name;
    private String desc;

    public RxOperatorBean(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
