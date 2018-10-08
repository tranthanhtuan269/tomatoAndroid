package com.tomato.tuantt.tomatoapp.model.Services;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceNoteTwo implements Serializable {

    @SerializedName("data")
    public List<DataNoteTwo> listService = new ArrayList<>();
}
