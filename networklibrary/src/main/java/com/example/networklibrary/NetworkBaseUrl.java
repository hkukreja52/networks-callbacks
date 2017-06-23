package com.example.networklibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.networklibrary.shared_preferences.PermanentPreferences;


/**
 * Created by Harsha on 6/22/2017.
 */

public class NetworkBaseUrl {

    private static NetworkBaseUrl instance;

    private Context context;
    private PreferenceManager privateManager;

    public static void init(Context context) {
        if (instance == null)
            instance = new NetworkBaseUrl(context);
    }

    public static void clearData() {
        if (instance != null) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(instance.context).edit();
            editor.clear();
            editor.apply();
        }
    }

    private NetworkBaseUrl(Context context) {
        this.context = context.getApplicationContext();

        privateManager = new PreferenceManager(context);
        privateManager.setSharedPreferencesName(PermanentPreferences.NAME);
        privateManager.setSharedPreferencesMode(Context.MODE_PRIVATE);
        privateManager.setStorageDeviceProtected();
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
        return instance.privateManager.getSharedPreferences().getString(PermanentPreferences.BASE_URL,"");
    }
}
