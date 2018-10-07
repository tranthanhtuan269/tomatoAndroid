package com.tomato.tuantt.tomatoapp.model;

public class Log {
    private String address;
    private String note;
    private String start_time;
    private String end_time;
    private int state;
    private int price;
    private Package aPackage;
    private String icon;

    public Log() {
    }

    public Log(String address, String note, String start_time, String end_time, int state, int price, Package aPackage, String icon) {
        this.address = address;
        this.note = note;
        this.start_time = start_time;
        this.end_time = end_time;
        this.state = state;
        this.price = price;
        this.aPackage = aPackage;
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
