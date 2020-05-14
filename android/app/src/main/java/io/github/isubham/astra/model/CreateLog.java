package io.github.isubham.astra.model;

public class CreateLog {
    public int person_id;
    public String location;
    public int type;

    public CreateLog(int person_id, String location, int type) {
        this.person_id = person_id;
        this.location = location;
        this.type = type;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
