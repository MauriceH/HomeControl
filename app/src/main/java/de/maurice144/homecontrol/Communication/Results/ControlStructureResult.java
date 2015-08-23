package de.maurice144.homecontrol.Communication.Results;

import android.content.Context;

import org.json.JSONObject;

import de.maurice144.homecontrol.Data.ControlGroup;
import de.maurice144.homecontrol.Data.ControlPage;

/**
 * Created by Maurice on 28.07.2015.
 */
public class ControlStructureResult extends DefaultResult {

    public ControlStructureResult(JSONObject data) {
        super(data);
    }

    public JSONObject getControlJsonObj() {
        return this.data;
    }





}
