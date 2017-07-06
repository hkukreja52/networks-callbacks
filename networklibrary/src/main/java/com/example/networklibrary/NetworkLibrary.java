package com.example.networklibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.networklibrary.network.data.inherit.Response;
import com.example.networklibrary.shared_preferences.PermanentPreferences;

import java.lang.reflect.Field;
import java.util.Arrays;


/**
 * Created by Harsha on 6/22/2017.
 */

public class NetworkLibrary {

    private static NetworkLibrary instance;

    private Context context;
    private PreferenceManager privateManager;

    public static void init(Context context, String baseurl) {
        if (instance == null)
            instance = new NetworkLibrary(context, baseurl);
    }


    public static <T extends Enum<T>> void setArray(T[] t) {
        System.out.println("*****" + Arrays.asList(t));
        System.out.print("====");
    }

    public static <T extends Enum<T>> void set(Class<T> t) {

        for (T t1 : t.getEnumConstants()) {
            System.out.println("***********" + t1.name());
            System.out.println("***********" + t1.toString());
        }
    }

    public static <T extends Enum<T>> void doSomething(Class<T> clazz) {
        //EnumSet<T> all = EnumSet.allOf(clazz);

        T[] t = clazz.getEnumConstants();
        for (int i = 0; i < clazz.getEnumConstants().length; i++)
            System.out.println("******Enum: " + t[i]);

        Field[] field = clazz.getDeclaredFields();
        for (int i = 0; i < clazz.getDeclaredFields().length; i++)
            System.out.println("******Fields: " + field[i]);

        for (int i = 0; i < clazz.getEnumConstants().length; i++) {
            Response.Type.values()[i] = Response.Type.valueOf(t[i].toString());
        }

        /*for (T constant : clazz.getEnumConstants()) {
            Response.Type.values()[Response.Type.values().length + 1] = Response.Type.valueOf(constant.name());
        }*/

        System.out.println("*****Size: " + Response.Type.values().length);
    }

    public static void clearData() {
        if (instance != null) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(instance.context).edit();
            editor.clear();
            editor.apply();
        }
    }

    public NetworkLibrary() {
    }

    private NetworkLibrary(Context context, String baseurl) {
        this.context = context.getApplicationContext();

        privateManager = new PreferenceManager(context);
        privateManager.setSharedPreferencesName(PermanentPreferences.NAME);
        privateManager.setSharedPreferencesMode(Context.MODE_PRIVATE);
        privateManager.setStorageDeviceProtected();
        privateManager.getSharedPreferences().edit().putString(PermanentPreferences.BASE_URL, baseurl).apply();
    }

    public static void restartApp() {
        if (instance != null) {
            Intent intent = instance.context.getPackageManager().getLaunchIntentForPackage(instance.context.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            instance.context.startActivity(intent);
        }
    }

    public static void setBaseUrl(String baseUrl) {
        instance.privateManager.getSharedPreferences().edit().putString(PermanentPreferences.BASE_URL, baseUrl).apply();
    }

    public static String getBaseUrl() {
        return instance.privateManager.getSharedPreferences().getString(PermanentPreferences.BASE_URL, "");
    }
}
