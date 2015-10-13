package de.maurice144.homecontrol.GCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmListenerService;

import de.maurice144.homecontrol.Communication.SynchronisationService;
import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.FrontEnd.Activity.StartActivity;
import de.maurice144.homecontrol.R;

public class CloudListenerService extends GcmListenerService {

    private static final String TAG = "CloudListenerService";

    private static final int MSG_TYPE_ID_LOGOFF = 1;
    private static final int MSG_TYPE_ID_CONTROLSTATE = 2;
    private static final int MSG_TYPE_ID_STRUCTURECHANGE = 3;
    private static final int MSG_TYPE_ID_TEXTMESSAGE = 4;

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
            case MSG_TYPE_ID_TEXTMESSAGE:
                displayTextNotification(data);
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
        intent.putExtra("mode", "controlstatechanged");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void controlStructureChanged(Bundle data) {
        startService(SynchronisationService.getServiceStartIntentByMode(this, SynchronisationService.STARTMODE_SyncStructure));
    }

    private void displayTextNotification(Bundle data) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.home)
                        .setContentTitle("HomeControl")
                        .setContentText(data.getString("text","Keine Angabe"));
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, StartActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(StartActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =    stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.addLine("A");
        inboxStyle.addLine("B");
        inboxStyle.addLine("C");
        inboxStyle.addLine("D");

        mBuilder.setStyle(inboxStyle);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.



        mNotificationManager.notify(8, mBuilder.build());
    }

}
