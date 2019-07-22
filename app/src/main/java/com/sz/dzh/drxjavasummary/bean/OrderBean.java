package com.sz.dzh.drxjavasummary.bean;

/**
 * Created by administrator on 2018/8/16.
 * sqlite测试Bean
 */
public class OrderBean {
    public int id;
    public String customName;
    public int orderPrice;
    public String country;

    public OrderBean() {
    }

    public OrderBean(int id, String customName, int orderPrice, String country) {
        this.id = id;
        this.customName = customName;
        this.orderPrice = orderPrice;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "id=" + id +
                ", customName='" + customName + '\'' +
                ", orderPrice=" + orderPrice +
                ", country='" + country + '\'' +
                '}';
    }
}
