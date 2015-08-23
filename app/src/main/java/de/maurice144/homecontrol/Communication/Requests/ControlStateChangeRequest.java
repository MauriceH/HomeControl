package de.maurice144.homecontrol.Communication.Requests;

/**
 * Created by Maurice on 23.08.2015.
 */
public class ControlStateChangeRequest extends SecureDefaultRequest {

    public ControlStateChangeRequest() {
        super();
    }

    public ControlStateChangeRequest(String deviceToken, int controlId, int state) {
        super(deviceToken);
        setControlId(controlId);
        setStateId(state);
    }

    public void setControlId(int controlId) {
        try {
            data.put("ControlId", controlId);
        }catch (Exception ex) {
        }
    }

    public void setStateId(int state) {
        try {
            data.put("State", state);
        }catch (Exception ex) {
        }
    }
}
