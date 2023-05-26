package model;

import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private SimpleStringProperty phone = new SimpleStringProperty("");
    private SimpleStringProperty email = new SimpleStringProperty("");
    private SimpleStringProperty address = new SimpleStringProperty("");

    public Contact() {
        this("no phone", "no email", "no address");
    }
    
    public Contact(String phone, String email, String address) {
        this.phone.set(phone);
        this.email.set(email);
        this.address.set(address);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }
    public String getEmail() {
        return email.get();
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public String getAddress() {
        return address.get();
    }
    public void setAddress(String address) {
        this.address.set(address);
    }
}
