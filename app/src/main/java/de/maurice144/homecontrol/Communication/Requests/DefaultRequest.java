package de.maurice144.homecontrol.Communication.Requests;

import org.json.JSONObject;

/**
 * Created by mhessing on 26.07.2015.
 */
public class DefaultRequest {

    protected JSONObject data;

    public DefaultRequest() {
        data = new JSONObject();
    }

    public JSONObject getJsonData() {
        return data;
    }

}
