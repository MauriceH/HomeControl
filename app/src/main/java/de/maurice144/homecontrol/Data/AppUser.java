package de.maurice144.homecontrol.Data;

/**
 * Created by mhessing on 24.07.2015.
 */
public class AppUser {

    private String id;
    private String fullName;

    public AppUser() {
    }

    public AppUser(LocalSettings settings) {
        this.id = settings.getUserId();
        this.fullName = settings.getUserFullName();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
