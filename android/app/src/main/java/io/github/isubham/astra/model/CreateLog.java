package io.github.isubham.astra.model;

public class CreateLog {

    private String person_id;
    private String location;
    private String type;

    public CreateLog(String person_id, String location, String type) {
        this.person_id = person_id;
        this.location = location;
        this.type = type;
    }
}
