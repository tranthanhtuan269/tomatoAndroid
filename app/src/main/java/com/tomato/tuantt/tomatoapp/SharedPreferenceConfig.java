package com.tomato.tuantt.tomatoapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceConfig {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String SHARE_NAME = "hsp_tomato";
    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    private SharedPreferences sharedPreferences;
    private static SharedPreferenceConfig config;
    public static SharedPreferenceConfig getInstance(Context context) {
        if (config == null) {
            config = new SharedPreferenceConfig(context);
        }
        return config;
    }

    private SharedPreferenceConfig(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_STATUS, status);
        editor.commit();
    }

    public boolean readLoginStatus(){
        boolean status = false;
        status = sharedPreferences.getBoolean(LOGIN_STATUS, status);
        return status;
    }

    public void saveToken(String token) {
        sharedPreferences.edit().putString(ACCESS_TOKEN,token).commit();
    }

    public String getToken(){
        return sharedPreferences.getString(ACCESS_TOKEN,null);
    }
}
