package de.maurice144.homecontrol.Communication.Requests;

/**
 * Created by Maurice on 23.08.2015.
 */
public class GcmTokenRequest extends SecureDefaultRequest {


    public GcmTokenRequest() {
        super();
    }

    public GcmTokenRequest(String deviceToken, String gcmToken) {
        super(deviceToken);
        setGcmToken(gcmToken);
    }

    public void setGcmToken(String gcmToken) {
        try {
            data.put("GcmToken", gcmToken);
        }catch (Exception ex) {
        }
    }



}
