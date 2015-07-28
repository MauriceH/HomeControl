package de.maurice144.homecontrol.Communication;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.maurice144.homecontrol.Communication.Requests.DefaultRequest;
import de.maurice144.homecontrol.Communication.Requests.SecureDefaultRequest;
import de.maurice144.homecontrol.Communication.Results.ControlStructureResult;
import de.maurice144.homecontrol.Data.ControlStructureJsonFile;
import de.maurice144.homecontrol.Data.LocalSettings;

/**
 * Created by Maurice on 28.07.2015.
 */
public class SynchronisationService extends IntentService {


    public static final String STARTMODE = "STARTMODE";

    public static final int STARTMODE_SyncStructure = 1001;


    public static Intent getServiceStartIntentByMode(Context context,int serviceStartMode) {
        Intent intent = new  Intent(context,SynchronisationService.class);
        intent.putExtra(STARTMODE,serviceStartMode);
        return intent;
    }


    //////////////////////////////////////////////////////////

    public SynchronisationService() {
        super("SynchronisationService");
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
        }
    }

    private void SyncControlStructure() {
        LocalSettings settings = new LocalSettings(this);
        WebApi webApi = new WebApi(this,settings);

        ControlStructureResult conStrcResult;
        try {
            conStrcResult  = webApi.ControlStructure(new SecureDefaultRequest(settings.getDeviceToken()));
        }catch (Exception ex) {
            Log.e("SControlStructure",ex.getMessage(),ex);
            return;
        }

        if(!conStrcResult.isDoneCorrect()) {
            Log.e("ControlStructure","Error sync control structure! Code: " + String.valueOf(conStrcResult.intErrorCode()));
            return;
        }

        if(conStrcResult.getControlJsonObj() == null) {
            Log.e("SControlStructure","Error structure empty!");
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

        settings.setStructureAvailable(true);
        settings.Save();

    }


}
