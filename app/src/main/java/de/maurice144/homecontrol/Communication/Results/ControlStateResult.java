package de.maurice144.homecontrol.Communication.Results;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

public class ControlStateResult extends DefaultResult {

    public ControlStateResult(JSONObject data) {
        super(data);
    }


    public JSONArray getControls() throws Exception {
        return data.getJSONArray("Controls");
    }
}
