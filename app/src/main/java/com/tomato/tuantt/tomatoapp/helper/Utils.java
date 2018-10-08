package com.tomato.tuantt.tomatoapp.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Utils {
    public static <T> T parserObject(String json, Class<T> typeClass) {
        Gson gson = new Gson();
        return gson.fromJson(json, new ObjectOfJson<T>(typeClass));
    }


    public static class ObjectOfJson<T> implements ParameterizedType {
        private Class<T> wrapped;

        public ObjectOfJson(Class<T> wrapper) {
            this.wrapped = wrapper;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        @Override
        public Type getRawType() {
            return wrapped;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


}
