package io.github.isubham.astra.model;

public class CreateLog {
    public int person_id;
    public int user_id;
    public String location;
    public int actionType;

    public CreateLog(int person_id, String location, int actionType) {
        this.person_id = person_id;
        this.location = location;
        this.actionType = actionType;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
}
