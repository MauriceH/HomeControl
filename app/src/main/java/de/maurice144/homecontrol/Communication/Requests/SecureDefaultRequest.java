package de.maurice144.homecontrol.Communication.Requests;

/**
 * Created by Maurice on 28.07.2015.
 */
public class SecureDefaultRequest extends DefaultRequest {

    public SecureDefaultRequest() {
        super();
    }

    public SecureDefaultRequest(String deviceToken) {
        super();
        setDeviceToken(deviceToken);
    }

    public void setDeviceToken(String deviceToken) {
        try {
            data.put("DeviceToken", deviceToken);
        }catch (Exception ex) {
        }
    }
}
