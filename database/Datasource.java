package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Contact;
import model.Doctor;
import model.Manager;
import model.Nurse;
import model.Pharmacist;
import model.Receptionist;
import model.Staff;
import model.enums.Gender;
import model.enums.Status;

public class Datasource {

    private static final String DB_NAME = "hospital.db";

    private static final String CONNECTION_STRING = "jdbc:sqlite:database\\"+ DB_NAME;

    private static final String TABLE_PERSON = "person";
    private static final String COLUMN_PERSON_ID = "id";
    private static final String COLUMN_PERSON_NAME = "name";
    private static final String COLUMN_PERSON_SURNAME = "surname";
    private static final String COLUMN_PERSON_GENDER = "gender";
    private static final String COLUMN_PERSON_AGE = "age";


    private static final String TABLE_STAFF = "staff";
    private static final String COLUMN_STAFF_ID = "id";
    private static final String COLUMN_STAFF_USERNAME = "username";
    private static final String COLUMN_STAFF_PASSWORD = "password";
    private static final String COLUMN_STAFF_STATUS = "status";
    private static final String COLUMN_STAFF_PERSON_ID = "person_id";

    private static final String TABLE_CONTACT = "contact";
    private static final String COLUMN_CONTACT_PERSON_ID = "person_id";
    private static final String COLUMN_CONTACT_PHONE = "phone";
    private static final String COLUMN_CONTACT_EMAIL = "email";
    private static final String COLUMN_CONTACT_ADDRESS = "address";

    private static final String TABLE_PATIENT = "patient";
    private static final String COLUMN_PATIENT_ID = "id";
    private static final String COLUMN_PATIENT_PERSON_ID = "person_id";
    private static final String COLUMN_PATIENT_STAFF_ID = "staff_id";
    private static final String COLUMN_PATIENT_RECEIPT_ID = "receipt_id";
    private static final String COLUMN_PATIENT_COMPLAINT = "complaint";
    private static final String COLUMN_PATIENT_APPOINTMENT = "appointment";
    private static final String COLUMN_PATIENT_EMERGENCY_STATE = "emergency_state";
    private static final String COLUMN_PATIENT_PRIORITY = "priority";
    private static final String COLUMN_PATIENT_BLOOD_TYPE = "blood_type";

    private static final String TABLE_RECEIPT = "receipt";
    private static final String COLUMN_RECEIPT_ID = "id";
    private static final String COLUMN_RECEIPT_PATIENT_ID = "patient_id";
    private static final String COLUMN_RECEIPT_STAFF_ID = "staff_id";
    private static final String COLUMN_RECEIPT_GIVEN_DATE = "given_date";
    private static final String COLUMN_RECEIPT_EXPIRE_DATE = "expire_date";

    private static final String TABLE_MEDICINE = "medicine";
    private static final String COLUMN_MEDICINE_ID = "id";
    private static final String COLUMN_MEDICINE_NAME = "name";
    private static final String COLUMN_MEDICINE_TYPE = "type";
    private static final String COLUMN_MEDICINE_STOCK = "stock";

    private static final String TABLE_MEDICINE_RECEIPT = "medicine_receipt";
    private static final String COLUMN_MEDICINE_RECEIPT_RECEIPT_ID = "id";
    private static final String COLUMN_MEDICINE_RECEIPT_MEDICINE_ID = "medicine_id";
    private static final String COLUMN_MEDICINE_RECEIPT_AMOUNT = "amount";

    private static final String QUERY_LOGIN = "SELECT " + COLUMN_STAFF_STATUS + " FROM " + TABLE_STAFF 
    + " WHERE " + COLUMN_STAFF_USERNAME + " = ? AND " + COLUMN_STAFF_PASSWORD + " = ?";

    private static final String QUERY_USER_BY_USERNAME = "SELECT * FROM " + TABLE_STAFF +
    " INNER JOIN " + TABLE_PERSON + " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_PERSON + "." + COLUMN_PERSON_ID +
    " INNER JOIN " + TABLE_CONTACT + " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_CONTACT + "." + COLUMN_CONTACT_PERSON_ID +
    " WHERE " + COLUMN_STAFF_USERNAME + " = ?";



    private Connection conn;

    private PreparedStatement queryLogin;
    private PreparedStatement queryUserByUsername;

    private static Datasource instance = new Datasource();

    private Datasource() {
    }

    public static Datasource getInstance() {
        return instance;
        // Datastore.getInstance().method() to call a method
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            queryLogin = conn.prepareStatement(QUERY_LOGIN);
            queryUserByUsername = conn.prepareStatement(QUERY_USER_BY_USERNAME);
            //insertIntoArtists = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {

            if (queryLogin != null) {
                queryLogin.close();
            }

            if (queryUserByUsername != null) {
                queryUserByUsername.close();
            }

            if (conn != null) {
                conn.close();
            }


        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    // return null if there is an error or the username and password don't match
    // in success, return the status of the staff in string
    public String queryLogin(String username, String password) {
        try {
            queryLogin.setString(1, username);
            queryLogin.setString(2, password);
            return queryLogin.executeQuery().getString(1);
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }



    public Staff queryProfile(String username){
        try {
            queryUserByUsername.setString(1, username);
            ResultSet results = queryUserByUsername.executeQuery();
            String status = results.getString(COLUMN_STAFF_STATUS);
            int id = results.getInt(COLUMN_STAFF_ID);
            String name = results.getString(COLUMN_PERSON_NAME);
            String surname = results.getString(COLUMN_PERSON_SURNAME);
            int age = results.getInt(COLUMN_PERSON_AGE);
            Gender gender = results.getString(COLUMN_PERSON_GENDER).equals("MALE") ? Gender.MALE : Gender.FEMALE;
            String phone = results.getString(COLUMN_CONTACT_PHONE);
            String email = results.getString(COLUMN_CONTACT_EMAIL);
            String address = results.getString(COLUMN_CONTACT_ADDRESS);
            Contact contact = new Contact(phone, email, address);
            Staff staff;
            switch(status){
                case "manager":
                    staff = new Manager();
                    staff.setStatus(Status.MANAGER);
                    break;
                case "doctor":
                    staff = new Doctor();
                    staff.setStatus(Status.DOCTOR);
                    break;
                case "nurse":
                    staff = new Nurse();
                    staff.setStatus(Status.NURSE);
                    break;
                case "pharmacist":
                    staff = new Pharmacist();
                    staff.setStatus(Status.PHARMACIST);
                    break;
                case "receptionist":
                    staff = new Receptionist();
                    staff.setStatus(Status.RECEPTIONIST);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + status);
            }
            staff.setAge(age);
            staff.setGender(gender);
            staff.setContact(contact);
            staff.setName(name);
            staff.setSurname(surname);
            staff.setId(id);
            return staff;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

}