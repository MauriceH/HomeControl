package de.maurice144.homecontrol.Communication;

import android.content.Context;

import org.json.JSONObject;

import de.maurice144.homecontrol.Communication.Requests.ControlStateChangeRequest;
import de.maurice144.homecontrol.Communication.Requests.GcmTokenRequest;
import de.maurice144.homecontrol.Communication.Requests.LoginRequest;
import de.maurice144.homecontrol.Communication.Requests.SecureDefaultRequest;
import de.maurice144.homecontrol.Communication.Results.ControlStructureResult;
import de.maurice144.homecontrol.Communication.Results.DefaultResult;
import de.maurice144.homecontrol.Communication.Results.LoginResult;
import de.maurice144.homecontrol.Data.LocalSettings;

public class WebApi {

    private RestApiConnection restcon;
    private Context context;
    private LocalSettings settings;

    public WebApi(Context context) {
        this.context = context;
        settings = new LocalSettings(context);
        restcon = new RestApiConnection(settings.getServerHostNameLocal(),settings.getServerPort());
    }

    public WebApi(Context context,LocalSettings settings) {
        this.context = context;
        this.settings = settings;
        restcon = new RestApiConnection(settings.getServerHostNameLocal(),settings.getServerPort());
    }


    public WebApi(Context context,String hostName, int port) {
        this.context = context;
        settings = new LocalSettings(context);
        restcon = new RestApiConnection(hostName, port);
    }




    public LoginResult Login(LoginRequest request) throws Exception {
        LoginResult actResult = null;
        JSONObject jsonResult = restcon.sendPostData("auth/login", request.getJsonData());
        if (jsonResult != null && !jsonResult.toString().equals(""))
            actResult = new LoginResult(jsonResult);
        return actResult;
    }


    public DefaultResult SendGcmToken(GcmTokenRequest request) throws Exception {
        LoginResult actResult = null;
        JSONObject jsonResult = restcon.sendPostData("auth/gcmtoken", request.getJsonData());
        if (jsonResult != null && !jsonResult.toString().equals(""))
            actResult = new LoginResult(jsonResult);
        return actResult;
    }


    public ControlStructureResult ControlStructure(SecureDefaultRequest request) throws Exception {
        ControlStructureResult cstResult = null;
        JSONObject jsonResult = restcon.sendPostData("control/structure", request.getJsonData());
        if (jsonResult != null && !jsonResult.toString().equals(""))
            cstResult = new ControlStructureResult(jsonResult);
        return cstResult;
    }

    public DefaultResult SendControlChange(ControlStateChangeRequest request) throws Exception {
        LoginResult actResult = null;
        JSONObject jsonResult = restcon.sendPostData("control/state/set", request.getJsonData());
        if (jsonResult != null && !jsonResult.toString().equals(""))
            actResult = new LoginResult(jsonResult);
        return actResult;
    }

    public boolean CheckConnection() {

        try {
            JSONObject jsonResult = restcon.callGet("test");
            if (jsonResult != null && !jsonResult.toString().equals("")) {
                DefaultResult result = new DefaultResult(jsonResult);
                return result.isDoneCorrect();
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }


}
