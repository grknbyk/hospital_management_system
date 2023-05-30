package model;

import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.enums.Gender;

abstract public class Person {
	private SimpleIntegerProperty id = new SimpleIntegerProperty();
	private SimpleStringProperty name = new SimpleStringProperty("");
	private SimpleStringProperty surname = new SimpleStringProperty("");
	private ObjectProperty<Gender> gender = new SimpleObjectProperty<>();
	private SimpleIntegerProperty age = new SimpleIntegerProperty();

	public int getId() {
		return id.get();
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getSurname() {
		return surname.get();
	}

	public void setSurname(String surname) {
		this.surname.set(surname);
	}

	public Gender getGender() {
		return gender.get();
	}

	public void setGender(Gender gender) {
		this.gender.set(gender);
	}

	public int getAge() {
		return age.get();
	}

	public void setAge(int age) {
		this.age.set(age);
	}

	@Override
	public String toString() {
		return getName().substring(0, 1).toUpperCase(Locale.US) + ". "
				+ getSurname().substring(0, 1).toUpperCase(Locale.US)
				+ getSurname().substring(1).toLowerCase(Locale.US);
	}
}
