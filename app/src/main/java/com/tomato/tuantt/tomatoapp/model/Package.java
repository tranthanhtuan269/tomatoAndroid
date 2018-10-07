package com.tomato.tuantt.tomatoapp.model;

import java.io.Serializable;

public class Package {
    private int Id;
    private String Name;
    private int Price;
    private String Icon;

    public Package() {
    }

    public Package(int id, String name, int price, String icon) {
        Id = id;
        Name = name;
        Price = price;
        Icon = icon;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }
}
