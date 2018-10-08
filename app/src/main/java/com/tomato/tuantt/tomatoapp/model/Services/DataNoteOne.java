package com.tomato.tuantt.tomatoapp.model.Services;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataNoteOne implements Serializable{
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String nameService;
    @SerializedName("icon")
    public String iconService;


    @SerializedName("services")
    public ServiceNoteTwo serviceNoteTwo;

}
