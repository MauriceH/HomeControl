package de.maurice144.homecontrol.Data;

import android.content.Context;
import android.content.SharedPreferences;

import de.maurice144.homecontrol.MainControlActivity;

/**
 * Created by mhessing on 24.07.2015.
 */
public class LocalSettings {

    private static final String prefFile ="de.maurice144.homecontrol.globalsettings";


    private Context context;


    private String deviceToken;
    private String userFullName;
    private long userId;
    private String gcmToken;



    public LocalSettings(Context context) {
        this.context = context;
        ReLoad();
    }

    public void ReLoad() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                prefFile, Context.MODE_PRIVATE);

        deviceToken  = sharedPref.getString("deviceToken",null);
        userFullName = sharedPref.getString("userFullName",null);
        userId = sharedPref.getLong("userId",-1);
        gcmToken = sharedPref.getString("gcmtoken",null);
    }

    public void Save() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("deviceToken",this.deviceToken);
        editor.putString("userFullName",this.userFullName);
        editor.putLong("userId", this.userId);
        editor.putString("gcmtoken",this.gcmToken);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return this.deviceToken != null;
    }


    public String getDeviceToken() {
        return deviceToken;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public Long getUserId() {
        return userId;
    }


    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }


    public void setNewActivation(String deviceToken, long userId, String userFullName) {
        this.deviceToken = deviceToken;
        this.userId = userId;
        this.userFullName = userFullName;
    }



}
