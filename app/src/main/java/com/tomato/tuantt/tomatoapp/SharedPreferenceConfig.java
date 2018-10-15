package com.tomato.tuantt.tomatoapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceConfig {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String SHARE_NAME = "hsp_tomato";
    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String USER_CODE = "USER_CODE";
    public static final String AVATAR_LINK = "AVATAR_LINK";
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
}


