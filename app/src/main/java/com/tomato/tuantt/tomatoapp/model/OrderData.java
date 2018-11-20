package com.tomato.tuantt.tomatoapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderData {

    private int id;

    private String address;

    private String note;

    private String start_time;

    private String end_time;

    private int state;

    private User user;

    private String price;

    private int pay_type;

    private String number_address;

    @SerializedName("package")
    private List<Package> mPackage;

    private String username;

    private String email;

    private String promotion_code;

    private int coupon_value;

    public List<Package> getmPackage() {
        return mPackage;
    }

    public void setmPackage(List<Package> mPackage) {
        this.mPackage = mPackage;
    }

    public int getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(int coupon_value) {
        this.coupon_value = coupon_value;
    }

    private String list_package;

    private Service service;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if(start_time.length() > 13) {
            this.start_time = start_time.substring(0,13);
        }else{
            this.start_time = start_time;
        }
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        if(end_time.length() > 13) {
            this.end_time = end_time.substring(0,13);
        }else{
            this.end_time = end_time;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getNumber_address() {
        return number_address;
    }

    public void setNumber_address(String number_address) {
        this.number_address = number_address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPromotion_code() {
        return promotion_code;
    }

    public void setPromotion_code(String promotion_code) {
        this.promotion_code = promotion_code;
    }

    public String getList_package() {
        return list_package;
    }

    public void setList_package(String list_package) {
        this.list_package = list_package;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public List<Package> getPackages() {
        return mPackage;
    }

    public void setPackages(List<Package> mPackage) {
        this.mPackage = mPackage;
    }
}
