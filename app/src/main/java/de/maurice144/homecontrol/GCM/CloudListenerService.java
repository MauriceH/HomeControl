package de.maurice144.homecontrol.GCM;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import de.maurice144.homecontrol.Communication.SynchronisationService;
import de.maurice144.homecontrol.Data.LocalSettings;

/**
 * Created by mhessing on 27.07.2015.
 */
public class CloudListenerService extends GcmListenerService {

    private static final String TAG = "CloudListenerService";

    private static final int MSG_TYPE_ID_LOGOFF = 1;
    private static final int MSG_TYPE_ID_CONTROLSTATE = 2;
    private static final int MSG_TYPE_ID_STRUCTURECHANGE = 3;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        int msgTypeId = Integer.valueOf(data.getString("mid"));

        switch (msgTypeId) {
            case MSG_TYPE_ID_LOGOFF:
                logOffUser();
                break;
            case MSG_TYPE_ID_CONTROLSTATE:
                controlStateChanged(data);
                break;
            case MSG_TYPE_ID_STRUCTURECHANGE:
                controlStructureChanged(data);
                break;
        }

    }

    private void logOffUser() {
        LocalSettings settings = new LocalSettings(this);
        settings.clearAccountData();
        settings.Save();
    }

    private void controlStateChanged(Bundle data) {
        Intent intent = new Intent("de.maurice144.homecontrol.event.control");
        intent.putExtras(data);
        intent.putExtra("mode","controlstatechanged");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void controlStructureChanged(Bundle data) {
        startService(SynchronisationService.getServiceStartIntentByMode(this,SynchronisationService.STARTMODE_SyncStructure ));
    }

}
