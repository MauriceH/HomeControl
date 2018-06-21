package de.maurice144.homecontrol.GCM;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import de.maurice144.homecontrol.Communication.SynchronisationService;
import de.maurice144.homecontrol.Data.LocalSettings;

public class RegistrationIntentService extends IntentService {

    public static final String API_PROJECT_ID = "984670671333";

    private static final String TAG = "RegistrationService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(API_PROJECT_ID,GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);
                setTokenRegistration(token);
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }


    private void setTokenRegistration(String token) {
        LocalSettings localSettings = new LocalSettings(this);
        localSettings.setGcmToken(token);
        startService(SynchronisationService.getServiceStartIntentByMode(this,SynchronisationService.STARTMODE_SendGcmToken));
    }


}
