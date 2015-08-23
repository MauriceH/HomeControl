package de.maurice144.homecontrol.Data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mhessing on 24.07.2015.
 */
public class LocalSettings {

    private static final String prefFile ="de.maurice144.homecontrol.globalsettings";


    private Context context;


    private String deviceToken;
    private String userFullName;
    private String userId;
    private String gcmToken;
    private boolean gcmTokenTransfered;

    private boolean structureAvailable;



    public LocalSettings(Context context) {
        this.context = context;
        ReLoad();
    }

    public void ReLoad() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                prefFile, Context.MODE_PRIVATE);

        deviceToken  = sharedPref.getString("deviceToken",null);
        userFullName = sharedPref.getString("userFullName",null);
        userId = sharedPref.getString("userId", null);
        gcmToken = sharedPref.getString("gcmtoken", null);
        structureAvailable = sharedPref.getBoolean("structureavailable", false);
        gcmTokenTransfered = sharedPref.getBoolean("gcmTokenTransfered",false);
    }

    public void Save() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("deviceToken",this.deviceToken);
        editor.putString("userFullName",this.userFullName);
        editor.putString("userId", this.userId);
        editor.putString("gcmtoken", this.gcmToken);
        editor.putBoolean("structureavailable", this.structureAvailable);
        editor.putBoolean("gcmTokenTransfered",this.gcmTokenTransfered);
        editor.apply();
    }



    public boolean isLoggedIn() {
        return this.deviceToken != null;
    }

    public boolean isGcmRegistered() {
        return this.gcmToken != null;
    }

    public boolean isStructureAvailable() {
        return this.structureAvailable;
    }

    public void setStructureAvailable(boolean structureAvailable) {
        this.structureAvailable = structureAvailable;
    }


    public String getDeviceToken() {
        return deviceToken;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getUserId() {
        return userId;
    }


    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
        this.gcmTokenTransfered = false;
    }
    public boolean isGcmTokenTransfered() {
        return gcmTokenTransfered;
    }

    public void setGcmTokenTransfered() {
        this.gcmTokenTransfered = true;
    }


    public void setNewActivation(String deviceToken, String userId, String userFullName) {
        this.deviceToken = deviceToken;
        this.userId = userId;
        this.userFullName = userFullName;
    }

    public void clearAccountData() {
        this.deviceToken = null;
        this.userId = null;
        this.userFullName = null;
        this.gcmTokenTransfered = false;
        this.structureAvailable = false;
    }



}
