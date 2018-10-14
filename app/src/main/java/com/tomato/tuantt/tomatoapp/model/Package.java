package com.tomato.tuantt.tomatoapp.model;

import com.google.gson.annotations.SerializedName;

public class Package {
    @SerializedName("id")
    private int Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("price")
    private String Price;
    @SerializedName("image")
    private String Icon;
    @SerializedName("service_id")
    private int serviceId;
    @SerializedName("pivot")
    private Pivot pivot;
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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }
}
