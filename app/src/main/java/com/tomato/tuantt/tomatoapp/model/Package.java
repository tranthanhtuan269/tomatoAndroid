package com.tomato.tuantt.tomatoapp.model;

import java.io.Serializable;

public class Package {
    private int Id;
    private String Name;
    private String Price;
    private String Icon;
    public int number = 0;
    public Package() {
    }

    public Package(int id, String name, String price, String icon) {
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }
}
