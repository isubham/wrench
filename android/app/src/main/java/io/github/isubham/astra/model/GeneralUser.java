package io.github.isubham.astra.model;

public class GeneralUser {

    private String profile_pic;
    private String username;
    private String name;
    private String first_name;
    private String father_name;
    private String email;
    private String dob;
    private String contact;
    private String aadhar_id;
    private String address;
    private String pincode;
    private String id_front;
    private String id_back;
    private String created_by;

    private String user_id;
    private String created;


    public GeneralUser(String profile_pic, String username, String name, String father_name, String email, String dob, String contact, String aadhar_id, String address, String pincode, String id_front, String id_back, String created_by) {
        this.profile_pic = profile_pic;
        this.username = username;
        this.name = name;
        this.father_name = father_name;
        this.email = email;
        this.dob = dob;
        this.contact = contact;
        this.aadhar_id = aadhar_id;
        this.address = address;
        this.pincode = pincode;
        this.id_front = id_front;
        this.id_back = id_back;
        this.created_by = created_by;
    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getAadhar_id() {
        return aadhar_id;
    }

    public void setAadhar_id(String aadhar_id) {
        this.aadhar_id = aadhar_id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId_front() {
        return id_front;
    }

    public void setId_front(String id_front) {
        this.id_front = id_front;
    }

    public String getId_back() {
        return id_back;
    }

    public void setId_back(String id_back) {
        this.id_back = id_back;
    }
}
