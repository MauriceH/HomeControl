package de.maurice144.homecontrol.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalSettings {

    private static final String prefFile ="de.maurice144.homecontrol.globalsettings";


    public static final String KEY_DEVICE_TOKEN = "deviceToken";
    public static final String KEY_GCM_TOKEN = "gcmtoken";
    public static final String KEY_STRUCTURE_AVAILABLE = "structureavailable";
    public static final String KEY_USER_FULL_NAME = "userFullName";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_GCM_TOKEN_TRANSFERED = "gcmTokenTransfered";
    public static final String KEY_SERVER_CONFIGURED = "serverConfigured";
    public static final String KEY_SERVER_HOST_NAME_LOCAL = "serverHostNameLocal";
    public static final String KEY_SERVER_HOST_NAME_REMOTE = "serverHostNameRemote";
    public static final String KEY_SERVER_PORT = "serverPort";

    private SharedPreferences prefs;

    public LocalSettings(Context context) {
        prefs = context.getSharedPreferences(prefFile, Context.MODE_MULTI_PROCESS);
    }



    public boolean isStructureAvailable() {
        return prefs.getBoolean(KEY_STRUCTURE_AVAILABLE, false);
    }

    public void setStructureAvailable(boolean structureAvailable) {
        SharedPreferences.Editor edit =  prefs.edit();
        edit.putBoolean(KEY_STRUCTURE_AVAILABLE, structureAvailable);
        edit.apply();
    }




    public String getUserFullName() {
        return prefs.getString(KEY_USER_FULL_NAME, null);
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }




    public String getGcmToken() {
        return prefs.getString(KEY_GCM_TOKEN, null);
    }

    public void setGcmToken(String gcmToken) {
        SharedPreferences.Editor edit =  prefs.edit();
        edit.putString(KEY_GCM_TOKEN, gcmToken);
        edit.putBoolean(KEY_GCM_TOKEN_TRANSFERED, false);
        edit.apply();
    }

    public boolean isGcmTokenTransfered() {
        return prefs.getBoolean(KEY_GCM_TOKEN_TRANSFERED, false);
    }

    public void setGcmTokenTransfered() {
        SharedPreferences.Editor edit =  prefs.edit();
        edit.putBoolean(KEY_GCM_TOKEN_TRANSFERED, true);
        edit.apply();
    }

    public boolean isGcmRegistrationObtained() {
        return getGcmToken() != null;
    }




    public String getDeviceToken() {
        return prefs.getString(KEY_DEVICE_TOKEN,null);
    }

    public boolean isLoggedIn() {
        return getDeviceToken() != null;
    }

    public void setNewActivation(String deviceToken, String userId, String userFullName) {
        SharedPreferences.Editor edit =  prefs.edit();
        edit.putString(KEY_DEVICE_TOKEN, deviceToken);
        edit.putString(KEY_USER_ID, userId);
        edit.putString(KEY_USER_FULL_NAME, userFullName);
        edit.apply();
    }

    public void clearAccountData() {
        SharedPreferences.Editor edit =  prefs.edit();
        edit.putString(KEY_DEVICE_TOKEN, null);
        edit.putString(KEY_USER_ID, null);
        edit.putString(KEY_USER_FULL_NAME, null);
        edit.putBoolean(KEY_GCM_TOKEN_TRANSFERED, false);
        edit.putBoolean(KEY_STRUCTURE_AVAILABLE, false);
        edit.apply();
    }



    public boolean isServerConfigured() {
        return prefs.getBoolean(KEY_SERVER_CONFIGURED, false);
    }

    public String getServerHostNameLocal() {
        return prefs.getString(KEY_SERVER_HOST_NAME_LOCAL, null);
    }

    public String getServerHostNameRemote() {
        return prefs.getString(KEY_SERVER_HOST_NAME_REMOTE, null);
    }

    public int getServerPort() {
        return prefs.getInt(KEY_SERVER_PORT, 10701);
    }

    public void setServerConfiguration(String hostNameLocal, String hostNameRemote,int port) {
        SharedPreferences.Editor edit =  prefs.edit();
        edit.putBoolean(KEY_SERVER_CONFIGURED, false);
        edit.putString(KEY_SERVER_HOST_NAME_LOCAL, hostNameLocal);
        edit.putString(KEY_SERVER_HOST_NAME_REMOTE, hostNameRemote);
        edit.putInt(KEY_SERVER_PORT, port);
        edit.apply();
    }

}
