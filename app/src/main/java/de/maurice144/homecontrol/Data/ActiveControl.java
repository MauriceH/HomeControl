package de.maurice144.homecontrol.Data;


public class ActiveControl {


    public String ControlType;
    public int Id;
    public String Title;
    public int State;

    public String getControlType() {
        return ControlType;
    }

    public void setControlType(String controlType) {
        ControlType = controlType;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }
}
