package de.maurice144.homecontrol.Communication;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Objects;

import de.maurice144.homecontrol.Communication.Requests.DefaultRequest;
import de.maurice144.homecontrol.Communication.Requests.GcmTokenRequest;
import de.maurice144.homecontrol.Communication.Requests.SecureDefaultRequest;
import de.maurice144.homecontrol.Communication.Results.ControlStructureResult;
import de.maurice144.homecontrol.Communication.Results.DefaultResult;
import de.maurice144.homecontrol.Data.ControlStructureJsonFile;
import de.maurice144.homecontrol.Data.LocalSettings;

/**
 * Created by Maurice on 28.07.2015.
 */
public class SynchronisationService extends IntentService {

    public Object syncObjectGcmSend;

    public static final String STARTMODE = "STARTMODE";

    public static final int STARTMODE_SyncStructure = 1001;
    public static final int STARTMODE_SendGcmToken = 1002;


    public static Intent getServiceStartIntentByMode(Context context,int serviceStartMode) {
        Intent intent = new  Intent(context,SynchronisationService.class);
        intent.putExtra(STARTMODE, serviceStartMode);
        return intent;
    }


    //////////////////////////////////////////////////////////

    public SynchronisationService() {
        super("SynchronisationService");
        syncObjectGcmSend = new Object();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent == null)
            return;

        int serviceRunMode;

        serviceRunMode = intent.getIntExtra(STARTMODE,-1);
        switch (serviceRunMode) {
            case STARTMODE_SyncStructure:
                SyncControlStructure();
                break;
            case STARTMODE_SendGcmToken:
                synchronized (syncObjectGcmSend) {
                    SendGcmToken();
                }
                break;
        }
    }

    private void SyncControlStructure() {
        LocalSettings settings = new LocalSettings(this);
        WebApi webApi = new WebApi(this,settings);


        settings.setStructureAvailable(false);



        ControlStructureResult conStrcResult;
        try {
            conStrcResult  = webApi.ControlStructure(new SecureDefaultRequest(settings.getDeviceToken()));
        }catch (Exception ex) {
            Log.e("SControlStructure",ex.getMessage(),ex);
            return;
        }

        if(!conStrcResult.isDoneCorrect()) {
            if(conStrcResult.AuthentificationNotValid()) {
                settings.clearAccountData();
            }
            Log.e("ControlStructure", "Error sync control structure! Code: " + String.valueOf(conStrcResult.intErrorCode()));
            return;
        }

        if(conStrcResult.getControlJsonObj() == null) {
            Log.e("ControlStructure","Error structure empty!");
            return;
        }

        ControlStructureJsonFile conStrcJsonFile;
        try {
            conStrcJsonFile = ControlStructureJsonFile.CreateByJsonObject(conStrcResult.getControlJsonObj());
            conStrcJsonFile.SaveFile();
        }catch (Exception ex) {
            Log.e("ControlStructure",ex.getMessage(),ex);
            return;
        }

        Intent intent = new Intent("de.maurice144.homecontrol.event.control");
        intent.putExtra("mode", "structurechanged");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);




        settings.setStructureAvailable(true);



    }

    private void SendGcmToken() {
        LocalSettings settings = new LocalSettings(this);
        WebApi webApi = new WebApi(this,settings);

        String token = settings.getGcmToken();

        if(token == null) {
            return;
        }

        try {
            DefaultResult result =  webApi.SendGcmToken(new GcmTokenRequest(settings.getDeviceToken(), token));

            if(result != null) {
                if(result.isDoneCorrect()) {
                    settings.setGcmTokenTransfered();
                } else{
                    if(result.AuthentificationNotValid()) {
                        settings.clearAccountData();
                    }
                }
            }

        }catch (Exception ex) {
            Log.e("SendGcmToken",ex.getMessage(),ex);
        }
    }
}
