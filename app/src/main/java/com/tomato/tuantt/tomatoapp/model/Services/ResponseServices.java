package com.tomato.tuantt.tomatoapp.model.Services;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseServices implements Serializable{
    @SerializedName("status_code")
    public int statusCode = 0;

    @SerializedName("message")
    public String message;

    @SerializedName("service")
    public ServiceNoteOne serviceNoteOne;
}
