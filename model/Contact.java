package model;

public class Contact {
    String phone;
    String email;
    String address;

    public Contact() {
        this("no phone", "no email", "no address");
    }
    
    public Contact(String phone, String email, String address) {
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
