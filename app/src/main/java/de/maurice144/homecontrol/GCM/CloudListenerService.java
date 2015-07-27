package de.maurice144.homecontrol.GCM;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import de.maurice144.homecontrol.Data.LocalSettings;

/**
 * Created by mhessing on 27.07.2015.
 */
public class CloudListenerService extends GcmListenerService {

    private static final String TAG = "CloudListenerService";

    private static final int MSG_TYPE_ID_LOGOFF = 1;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        int msgTypeId = Integer.valueOf(data.getString("mid")).intValue();

        switch (msgTypeId) {
            case MSG_TYPE_ID_LOGOFF:
                logOffUser();
                break;
        }

    }

    private void logOffUser() {
        LocalSettings settings = new LocalSettings(this);
        settings.clearAccountData();
        settings.Save();
    }

}
