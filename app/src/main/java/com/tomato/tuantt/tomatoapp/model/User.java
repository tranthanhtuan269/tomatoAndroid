package com.tomato.tuantt.tomatoapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id = -1;

    private String address;

    private String name;

    private String display_name;

    private String avatar;

    private String email;

    private String phone;

    private int city_id;

    private int role_id;

    private int active;

    private String presenter_id;

    private String code;

    private String coin;

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    private String created_at;

    private String updated_at;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getPresenter_id() {
        return presenter_id;
    }

    public void setPresenter_id(String presenter_id) {
        this.presenter_id = presenter_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.address);
        dest.writeString(this.name);
        dest.writeString(this.display_name);
        dest.writeString(this.avatar);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeInt(this.city_id);
        dest.writeInt(this.role_id);
        dest.writeInt(this.active);
        dest.writeString(this.presenter_id);
        dest.writeString(this.code);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.address = in.readString();
        this.name = in.readString();
        this.display_name = in.readString();
        this.avatar = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.city_id = in.readInt();
        this.role_id = in.readInt();
        this.active = in.readInt();
        this.presenter_id = in.readString();
        this.code = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
