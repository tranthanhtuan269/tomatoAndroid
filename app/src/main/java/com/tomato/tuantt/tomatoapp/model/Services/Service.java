package com.tomato.tuantt.tomatoapp.model.Services;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Service implements Serializable {
    @SerializedName("id")
    public int Id;
    @SerializedName("icon")
    public String Name;
    @SerializedName("name")
    public String Icon;
    @SerializedName("parent_id")
    public String parent_id;

    public Service() {
    }

    public Service(int id, String name, String icon) {
        Id = id;
        Name = name;
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

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }
}
