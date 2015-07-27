package de.maurice144.homecontrol.Communication;

import android.content.Context;

import org.json.JSONObject;

import de.maurice144.homecontrol.Communication.Requests.LoginRequest;
import de.maurice144.homecontrol.Communication.Results.DefaultResult;
import de.maurice144.homecontrol.Communication.Results.LoginResult;
import de.maurice144.homecontrol.Data.LocalSettings;

/**
 * Created by mhessing on 26.07.2015.
 */
public class WebApi {

    private RestApiConnection restcon;
    private Context context;
    private LocalSettings settings;

    public WebApi(Context context) {
        this.context = context;
        restcon = new RestApiConnection();
        settings = new LocalSettings(context);
    }

    public WebApi(Context context,LocalSettings settings) {
        this.context = context;
        restcon = new RestApiConnection();
        this.settings = settings;
    }


    public LoginResult Login(LoginRequest request) throws Exception {
        LoginResult actResult = null;
        JSONObject jsonResult = restcon.sendPostData("login.php", request.getJsonData());
        if (jsonResult != null && !jsonResult.toString().equals(""))
            actResult = new LoginResult(jsonResult);
        return actResult;
    }

}
