package model;

/**
 * @author : Gihan Madhusankha
 * 2022-07-12 10:10 AM
 **/

public class Student {
    private String stId;
    private String stName;
    private String email;
    private String contact;
    private String address;
    private String nic;

    public Student() {
    }

    public Student(String stId, String stName, String email, String contact, String address, String nic) {
        this.stId = stId;
        this.stName = stName;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.nic = nic;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stId='" + stId + '\'' +
                ", stName='" + stName + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", nic='" + nic + '\'' +
                '}';
    }
}
