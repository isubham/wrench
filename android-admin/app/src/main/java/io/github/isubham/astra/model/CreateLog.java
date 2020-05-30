package io.github.isubham.astra.model;

public class CreateLog {
    private int person_id;
    private String location;
    private int type;
    private String purpose;

    public CreateLog(int person_id, String location, int type, String purpose) {
        this.person_id = person_id;
        this.location = location;
        this.type = type;
        this.purpose = purpose;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
