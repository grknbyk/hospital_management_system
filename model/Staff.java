package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.enums.Status;

public abstract class Staff extends Person {
	private SimpleStringProperty username = new SimpleStringProperty("");
	private SimpleStringProperty password = new SimpleStringProperty("");
	private ObjectProperty<Status> status = new SimpleObjectProperty<>();
	private Contact contact;
	 
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Status getStatus() {
		return status.get();
	}

	public void setStatus(Status status) {
		this.status.set(status);
	}

	public String getUsername() {
		return username.get();
	}

	public void setUsername(String username) {
		this.username.set(username);
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password) {
		this.password.set(password);
	}


	public String getPhone() {
		return contact.getPhone();
	}
	public String getEmail() {
		return contact.getEmail();
	}
	public String getAddress() {
		return contact.getAddress();
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
