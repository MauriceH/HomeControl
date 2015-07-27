package de.maurice144.homecontrol.Communication.Results;

import org.json.JSONObject;

/**
 * Created by Maurice on 27.07.2015.
 */
public class LoginResult extends DefaultResult{

    public LoginResult(JSONObject data) {
        super(data);
    }

    public long getUserId() {
        return data.optLong("userid",-1l);
    }

    public String getFullName() {
        return data.optString("fullname", null);
    }

    public String getDeviceToken() {
        return data.optString("devicetoken", null);
    }

}
