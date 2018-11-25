package com.tomato.tuantt.tomatoapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceConfig {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String SHARE_NAME = "hsp_tomato";
    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String USER_CODE = "USER_CODE";
    public static final String AVATAR_LINK = "AVATAR_LINK";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String USER_NAME = "USER_NAME";
    private static final String EMAIL = "EMAIL";
    private static final String COIN = "COIN";
    private static final String PRESENT_ID = "PRESENT_ID";
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
        status = sharedPreferences.getBoolean(LOGIN_STATUS, false);
        return status;
    }

    public void saveToken(String token) {
        sharedPreferences.edit().putString(ACCESS_TOKEN,token).commit();
    }

    public String getToken(){
        return sharedPreferences.getString(ACCESS_TOKEN,"");
    }


    public void saveUserCode(String code) {
        sharedPreferences.edit().putString(USER_CODE,code).commit();
    }

    public String getUserCode(){
        return sharedPreferences.getString(USER_CODE,"");
    }

    public void saveAvatarLink(String link){
        sharedPreferences.edit().putString(AVATAR_LINK,link).commit();
    }

    public String getAvatarLink(){
        return sharedPreferences.getString(AVATAR_LINK,"");
    }

    public void setPhoneNumber(String phoneNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PHONE_NUMBER, phoneNumber).apply();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString(PHONE_NUMBER, "");
    }

    public void setUserName(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, username).apply();
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public void setPresentId(String presentId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRESENT_ID, presentId).apply();
    }

    public String getPresentId() {
        return sharedPreferences.getString(PRESENT_ID, "");
    }

    public void setCoin(String coin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COIN, coin).apply();
    }

    public String getCoin() {
        return sharedPreferences.getString(COIN, "");
    }
}


