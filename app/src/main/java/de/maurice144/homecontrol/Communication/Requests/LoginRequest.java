package de.maurice144.homecontrol.Communication.Requests;

/**
 * Created by mhessing on 26.07.2015.
 */
public class LoginRequest extends DefaultRequest {

    public void setUsername(String username) {
        try {
            data.put("user", username);
        }catch (Exception ex) {
        }
    }

    public void setPassword(String password) {
        try {
            data.put("pass", password);
        }catch (Exception ex) {
        }
    }

}
