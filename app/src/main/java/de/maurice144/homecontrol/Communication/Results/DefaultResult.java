package de.maurice144.homecontrol.Communication.Results;

import org.json.JSONObject;

import de.maurice144.homecontrol.Communication.Requests.DefaultRequest;

/**
 * Created by mhessing on 26.07.2015.
 */
public class DefaultResult {

    protected JSONObject data;

    public DefaultResult(JSONObject data) {
        this.data = data;
    }

    public boolean isDoneCorrect() {
        return data.optBoolean("Ok",false);
    }

    public int intErrorCode() {
        return data.optInt("ErrCode", 0);
    }

    public boolean AuthentificationNotValid() {
        return intErrorCode() == 403;
    }

}
