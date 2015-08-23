package de.maurice144.homecontrol.Communication.Results;

import org.json.JSONObject;

/**
 * Created by Maurice on 27.07.2015.
 */
public class LoginResult extends DefaultResult{

    public LoginResult(JSONObject data) {
        super(data);
    }

    public String getUserId() {
        return data.optString("UserId",null);
    }

    public String getFullName() {
        return data.optString("FullName", null);
    }

    public String getDeviceToken() {
        return data.optString("DeviceToken", null);
    }

}
