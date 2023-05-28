package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import model.Contact;
import model.Doctor;
import model.Manager;
import model.Medicine;
import model.MedicineSupply;
import model.Nurse;
import model.Patient;
import model.Person;
import model.Pharmacist;
import model.Receipt;
import model.Receptionist;
import model.Staff;
import model.enums.BloodType;
import model.enums.EmergencyState;
import model.enums.Gender;
import model.enums.MedicineType;
import model.enums.Priority;
import model.enums.Status;
import utils.List;

public class Datasource {

    private static final String DB_NAME = "hospital.db";

    private static final String CONNECTION_STRING = "jdbc:sqlite:database\\" + DB_NAME;

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

    private static final String TABLE_DOCTOR = "doctor";
    private static final String COLUMN_DOCTOR_STAFF_ID = "staff_id";
    private static final String COLUMN_DOCTOR_EXPRETISE = "expretise";

    private static final String TABLE_NURSE = "nurse";
    private static final String COLUMN_NURSE_STAFF_ID = "staff_id";
    private static final String COLUMN_NURSE_WORKING_AREA = "working_area";

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

    private static final String TABLE_MEDICINE_STOCK = "medicine_stock";
    private static final String COLUMN_MEDICINE_STOCK_MEDICINE_ID = "medicine_id";
    private static final String COLUMN_MEDICINE_STOCK_AMOUNT = "stock";

    private static final String TABLE_RECEIPT_MEDICINE = "receipt_medicine";
    private static final String COLUMN_RECEIPT_MEDICINE_RECEIPT_ID = "receipt_id";
    private static final String COLUMN_RECEIPT_MEDICINE_MEDICINE_ID = "medicine_id";
    private static final String COLUMN_RECEIPT_MEDICINE_AMOUNT = "medicine_amount";

    private static final String QUERY_LOGIN = "SELECT " + COLUMN_STAFF_STATUS + " FROM " + TABLE_STAFF
            + " WHERE " + COLUMN_STAFF_USERNAME + " = ? AND " + COLUMN_STAFF_PASSWORD + " = ?";

    private static final String QUERY_STAFF_BY_USERNAME = "SELECT * FROM " + TABLE_STAFF +
            " INNER JOIN " + TABLE_PERSON + " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_PERSON
            + "." + COLUMN_PERSON_ID +
            " INNER JOIN " + TABLE_CONTACT + " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_CONTACT
            + "." + COLUMN_CONTACT_PERSON_ID +
            " WHERE " + COLUMN_STAFF_USERNAME + " = ?";
    
    private static final String QUERY_STAFF_USERNAME_BY_ID = "SELECT " + COLUMN_STAFF_USERNAME + " FROM " + TABLE_STAFF +
            " WHERE " + COLUMN_STAFF_ID + " = ?";

    private static final String QUERY_MEDICINE = "SELECT * FROM " + TABLE_MEDICINE +
            " INNER JOIN " + TABLE_MEDICINE_STOCK + " ON " +
            TABLE_MEDICINE + "." + COLUMN_MEDICINE_ID + " = " + TABLE_MEDICINE_STOCK + "."
            + COLUMN_MEDICINE_STOCK_MEDICINE_ID;

    private static final String QUERY_DOCTORS = " SELECT " +
            TABLE_STAFF + "." + COLUMN_STAFF_ID + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_USERNAME + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_PASSWORD + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_STATUS + ", " +
            TABLE_DOCTOR + "." + COLUMN_DOCTOR_EXPRETISE + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_NAME + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_SURNAME + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_AGE + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_GENDER + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_PHONE + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_EMAIL + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_ADDRESS +
            " FROM " + TABLE_STAFF +
            " INNER JOIN " + TABLE_PERSON +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_PERSON + "." + COLUMN_PERSON_ID +
            " INNER JOIN " + TABLE_CONTACT +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_CONTACT + "." + COLUMN_CONTACT_PERSON_ID
            +
            " INNER JOIN " + TABLE_DOCTOR +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_ID + " = " + TABLE_DOCTOR + "." + COLUMN_DOCTOR_STAFF_ID +
            " WHERE " + COLUMN_STAFF_STATUS + " = 'doctor'" +
            " ORDER BY " + TABLE_STAFF + "." + COLUMN_STAFF_ID + " ASC";

    private static final String QUERY_NURSE = " SELECT " +
            TABLE_STAFF + "." + COLUMN_STAFF_ID + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_USERNAME + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_PASSWORD + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_STATUS + ", " +
            TABLE_NURSE + "." + COLUMN_NURSE_WORKING_AREA + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_NAME + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_SURNAME + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_AGE + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_GENDER + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_PHONE + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_EMAIL + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_ADDRESS +
            " FROM " + TABLE_STAFF +
            " INNER JOIN " + TABLE_PERSON +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_PERSON + "." + COLUMN_PERSON_ID +
            " INNER JOIN " + TABLE_CONTACT +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_CONTACT + "." + COLUMN_CONTACT_PERSON_ID
            +
            " INNER JOIN " + TABLE_NURSE +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_ID + " = " + TABLE_NURSE + "." + COLUMN_NURSE_STAFF_ID +
            " WHERE " + COLUMN_STAFF_STATUS + " = 'nurse'" +
            " ORDER BY " + TABLE_STAFF + "." + COLUMN_STAFF_ID + " ASC";

    private static final String QUERY_PHARMACIST = " SELECT " +
            TABLE_STAFF + "." + COLUMN_STAFF_ID + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_USERNAME + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_PASSWORD + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_STATUS + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_NAME + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_SURNAME + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_AGE + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_GENDER + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_PHONE + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_EMAIL + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_ADDRESS +
            " FROM " + TABLE_STAFF +
            " INNER JOIN " + TABLE_PERSON +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_PERSON + "." + COLUMN_PERSON_ID +
            " INNER JOIN " + TABLE_CONTACT +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_CONTACT + "." + COLUMN_CONTACT_PERSON_ID
            +
            " WHERE " + COLUMN_STAFF_STATUS + " = 'pharmacist'" +
            " ORDER BY " + TABLE_STAFF + "." + COLUMN_STAFF_ID + " ASC";

    private static final String QUERY_RECEPSIONIST = " SELECT " +
            TABLE_STAFF + "." + COLUMN_STAFF_ID + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_USERNAME + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_PASSWORD + ", " +
            TABLE_STAFF + "." + COLUMN_STAFF_STATUS + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_NAME + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_SURNAME + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_AGE + ", " +
            TABLE_PERSON + "." + COLUMN_PERSON_GENDER + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_PHONE + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_EMAIL + ", " +
            TABLE_CONTACT + "." + COLUMN_CONTACT_ADDRESS +
            " FROM " + TABLE_STAFF +
            " INNER JOIN " + TABLE_PERSON +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_PERSON + "." + COLUMN_PERSON_ID +
            " INNER JOIN " + TABLE_CONTACT +
            " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_CONTACT + "." + COLUMN_CONTACT_PERSON_ID
            +
            " WHERE " + COLUMN_STAFF_STATUS + " = 'recepsionist'" +
            " ORDER BY " + TABLE_STAFF + "." + COLUMN_STAFF_ID + " ASC";

    private static final String QUERY_STAFF_BY_ID = "SELECT id,person_id FROM " + TABLE_STAFF +
            " WHERE " + COLUMN_STAFF_ID + " = ?";

    private static final String QUERY_STAFF_ID_BY_USERNAME = "SELECT " + COLUMN_STAFF_ID +
            " FROM " + TABLE_STAFF +
            " WHERE " + COLUMN_STAFF_USERNAME + " = ?";

    private static final String QUERY_DOCTOR_EXPRETISE_BY_STAFF_ID = " SELECT " + COLUMN_DOCTOR_EXPRETISE +
            " FROM " + TABLE_DOCTOR +
            " WHERE " + COLUMN_DOCTOR_STAFF_ID + " = ?";

    private static final String QUERY_NURSE_WORKING_AREA_BY_STAFF_ID = " SELECT " + COLUMN_NURSE_WORKING_AREA +
            " FROM " + TABLE_NURSE +
            " WHERE " + COLUMN_NURSE_STAFF_ID + " = ?";

    private static final String QUERY_PATIENTS_BY_STAFF_ID = " SELECT * FROM " + TABLE_PATIENT +
            " INNER JOIN " + TABLE_PERSON +
            " ON " + TABLE_PATIENT + "." + COLUMN_PATIENT_PERSON_ID + " = " + TABLE_PERSON + "." + COLUMN_PERSON_ID +
            " WHERE " + COLUMN_PATIENT_STAFF_ID + " = ?";

    private static final String DELETE_CONTACT_BY_PERSON_ID = "DELETE FROM " + TABLE_CONTACT +
            " WHERE " + COLUMN_CONTACT_PERSON_ID + " = ?";

    private static final String DELETE_DOCTOR_BY_STAFF_ID = "DELETE FROM " + TABLE_DOCTOR +
            " WHERE " + COLUMN_DOCTOR_STAFF_ID + " = ?";

    private static final String DELETE_NURSE_BY_STAFF_ID = "DELETE FROM " + TABLE_NURSE +
            " WHERE " + COLUMN_NURSE_STAFF_ID + " = ?";

    private static final String DELETE_MEDICINE_BY_ID = "DELETE FROM " + TABLE_MEDICINE +
            " WHERE " + COLUMN_MEDICINE_ID + " = ?";

    private static final String DELETE_MEDICINE_STOCK_BY_MEDICINE_ID = "DELETE FROM " + TABLE_MEDICINE_STOCK +
            " WHERE " + COLUMN_MEDICINE_STOCK_MEDICINE_ID + " = ?";

    private static final String DELETE_PATIENT_BY_PERSON_ID = "DELETE FROM " + TABLE_PATIENT +
            " WHERE " + COLUMN_PATIENT_PERSON_ID + " = ?";

    private static final String DELETE_PERSON_BY_ID = "DELETE FROM " + TABLE_PERSON +
            " WHERE " + COLUMN_PERSON_ID + " = ?";

    private static final String DELETE_STAFF_BY_ID = "DELETE FROM " + TABLE_STAFF +
            " WHERE " + COLUMN_STAFF_ID + " = ?";

    private static final String DELETE_RECEIPT_BY_ID = "DELETE FROM " + TABLE_RECEIPT +
            " WHERE " + COLUMN_RECEIPT_ID + " = ?";

    private static final String DELETE_RECEIPT_MEDICINE_BY_RECEIPT_ID = "DELETE FROM " + TABLE_RECEIPT_MEDICINE +
            " WHERE " + COLUMN_RECEIPT_MEDICINE_RECEIPT_ID + " = ?";

    private static final String UPDATE_PERSON_BY_PERSON_ID = "UPDATE " + TABLE_PERSON + " SET " +
            COLUMN_PERSON_NAME + " = ?, " +
            COLUMN_PERSON_SURNAME + " = ?, " +
            COLUMN_PERSON_GENDER + " = ?, " +
            COLUMN_PERSON_AGE + " = ? " +
            " WHERE " + COLUMN_PERSON_ID + " = ?";

    private static final String UPDATE_CONTACT_BY_PERSON_ID = "UPDATE " + TABLE_CONTACT + " SET " +
            COLUMN_CONTACT_PHONE + " = ?, " +
            COLUMN_CONTACT_EMAIL + " = ?, " +
            COLUMN_CONTACT_ADDRESS + " = ? " +
            " WHERE " + COLUMN_CONTACT_PERSON_ID + " = ?";

    private static final String UPDATE_MEDICINE_BY_MEDICINE_ID = "UPDATE " + TABLE_MEDICINE + " SET " +
            COLUMN_MEDICINE_NAME + " = ?, " +
            COLUMN_MEDICINE_TYPE + " = ? " +
            " WHERE " + COLUMN_MEDICINE_ID + " = ?";

    private static final String UPDATE_MEDICINE_STOCK_BY_MEDICINE_ID = "UPDATE " + TABLE_MEDICINE_STOCK + " SET " +
            COLUMN_MEDICINE_STOCK_AMOUNT + " = ? " +
            " WHERE " + COLUMN_MEDICINE_STOCK_MEDICINE_ID + " = ?";

    private static final String UPDATE_STAFF_BY_STAFF_ID = "UPDATE " + TABLE_STAFF + " SET " +
            COLUMN_STAFF_USERNAME + " = ?, " +
            COLUMN_STAFF_PASSWORD + " = ? " +
            " WHERE " + COLUMN_STAFF_ID + " = ?";

    private static final String UPDATE_DOCTOR_BY_STAFF_ID = "UPDATE " + TABLE_DOCTOR + " SET " +
            COLUMN_DOCTOR_EXPRETISE + " = ? " +
            " WHERE " + COLUMN_DOCTOR_STAFF_ID + " = ?";

    private static final String UPDATE_NURSE_BY_STAFF_ID = "UPDATE " + TABLE_NURSE + " SET " +
            COLUMN_NURSE_WORKING_AREA + " = ? " +
            " WHERE " + COLUMN_NURSE_STAFF_ID + " = ?";

    private static final String INSERT_PERSON = "INSERT INTO " + TABLE_PERSON +
            " VALUES (?,?,?,?,?)";

    private static final String INSERT_CONTACT = "INSERT INTO " + TABLE_CONTACT +
            " VALUES (?,?,?,?)";

    private static final String INSERT_STAFF = "INSERT INTO " + TABLE_STAFF +
            " (" + COLUMN_STAFF_USERNAME + "," + COLUMN_STAFF_PASSWORD + "," + COLUMN_STAFF_STATUS + ") " +
            " VALUES (?,?,?)";

    private static final String INSERT_DOCTOR = "INSERT INTO " + TABLE_DOCTOR +
            " VALUES (?,?)";

    private static final String INSERT_NURSE = "INSERT INTO " + TABLE_NURSE +
            " VALUES (?,?)";

    private static final String INSERT_PATIENT = "INSERT INTO " + TABLE_PATIENT +
            " VALUES (?,?,?,?,?,?,?,?)";

    private static final String INSERT_MEDICINE = "INSERT INTO " + TABLE_MEDICINE +
            " VALUES (?,?,?)";

    private static final String INSERT_MEDICINE_STOCK = "INSERT INTO " + TABLE_MEDICINE_STOCK +
            " VALUES (?,?)";

    private static final String INSERT_RECEIPT = "INSERT INTO " + TABLE_RECEIPT +
            " VALUES (?,?,?,?,?,?)";

    private static final String INSERT_RECEIPT_MEDICINE = "INSERT INTO " + TABLE_RECEIPT_MEDICINE +
            " VALUES (?,?,?)";

    private static final String QUERY_LAST_PERSON_ID = "SELECT MAX(" + COLUMN_PERSON_ID + ") FROM " + TABLE_PERSON;
    private static final String QUERY_LAST_STAFF_ID = "SELECT MAX(" + COLUMN_STAFF_ID + ") FROM " + TABLE_STAFF;
    private static final String QUERY_LAST_MEDICINE_ID = "SELECT MAX(" + COLUMN_MEDICINE_ID + ") FROM " + TABLE_MEDICINE;
    private static final String QUERY_LAST_RECEIPT_ID = "SELECT MAX(" + COLUMN_RECEIPT_ID + ") FROM " + TABLE_RECEIPT;

    private Connection conn;

    private ArrayList<PreparedStatement> preparedStatements;
    private PreparedStatement queryLogin;
    private PreparedStatement queryStaffByUsername;
    private PreparedStatement queryStaffUsernameById;
    private PreparedStatement queryStaffIdByUsername;
    private PreparedStatement queryPatientsByStaffId;
    private PreparedStatement queryMedicine;
    private PreparedStatement queryStaffById;
    private PreparedStatement queryDoctorExpretiseByStaffId;
    private PreparedStatement queryNurseWorkingAreaByStaffId;
    private PreparedStatement deleteContactByPersonId;
    private PreparedStatement deleteDoctorByStaffId;
    private PreparedStatement deleteNurseByStaffId;
    private PreparedStatement deleteMedicineById;
    private PreparedStatement deleteMedicineStockByMedicineId;
    private PreparedStatement deletePatientByPersonId;
    private PreparedStatement deletePersonById;
    private PreparedStatement deleteStaffById;
    private PreparedStatement deleteReceiptById;
    private PreparedStatement deleteReceiptMedicineByReceiptId;
    private PreparedStatement updatePersonByPersonId;
    private PreparedStatement updateContactByPersonId;
    private PreparedStatement updateMedicineByMedicineId;
    private PreparedStatement updateMedicineStockByMedicineId;
    private PreparedStatement updateStaffByStaffId;
    private PreparedStatement updateDoctorByStaffId;
    private PreparedStatement updateNurseByStaffId;
    private PreparedStatement insertPerson;
    private PreparedStatement insertContact;
    private PreparedStatement insertStaff;
    private PreparedStatement insertDoctor;
    private PreparedStatement insertNurse;
    private PreparedStatement insertPatient;
    private PreparedStatement insertMedicine;
    private PreparedStatement insertMedicineStock;
    private PreparedStatement insertReceipt;
    private PreparedStatement insertReceiptMedicine;

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

            preparedStatements = new ArrayList<>();
            queryLogin = conn.prepareStatement(QUERY_LOGIN);
            preparedStatements.add(queryLogin);
            queryStaffByUsername = conn.prepareStatement(QUERY_STAFF_BY_USERNAME);
            preparedStatements.add(queryStaffByUsername);
            queryStaffUsernameById = conn.prepareStatement(QUERY_STAFF_USERNAME_BY_ID);
            preparedStatements.add(queryStaffUsernameById);
            queryStaffIdByUsername = conn.prepareStatement(QUERY_STAFF_ID_BY_USERNAME);
            preparedStatements.add(queryStaffIdByUsername);
            queryPatientsByStaffId = conn.prepareStatement(QUERY_PATIENTS_BY_STAFF_ID);
            preparedStatements.add(queryPatientsByStaffId);
            queryMedicine = conn.prepareStatement(QUERY_MEDICINE);
            preparedStatements.add(queryMedicine);
            queryStaffById = conn.prepareStatement(QUERY_STAFF_BY_ID);
            preparedStatements.add(queryStaffById);
            queryDoctorExpretiseByStaffId = conn.prepareStatement(QUERY_DOCTOR_EXPRETISE_BY_STAFF_ID);
            preparedStatements.add(queryDoctorExpretiseByStaffId);
            queryNurseWorkingAreaByStaffId = conn.prepareStatement(QUERY_NURSE_WORKING_AREA_BY_STAFF_ID);
            preparedStatements.add(queryNurseWorkingAreaByStaffId);
            deleteContactByPersonId = conn.prepareStatement(DELETE_CONTACT_BY_PERSON_ID);
            preparedStatements.add(deleteContactByPersonId);
            deleteDoctorByStaffId = conn.prepareStatement(DELETE_DOCTOR_BY_STAFF_ID);
            preparedStatements.add(deleteDoctorByStaffId);
            deleteNurseByStaffId = conn.prepareStatement(DELETE_NURSE_BY_STAFF_ID);
            preparedStatements.add(deleteNurseByStaffId);
            deleteMedicineById = conn.prepareStatement(DELETE_MEDICINE_BY_ID);
            preparedStatements.add(deleteMedicineById);
            deleteMedicineStockByMedicineId = conn.prepareStatement(DELETE_MEDICINE_STOCK_BY_MEDICINE_ID);
            preparedStatements.add(deleteMedicineStockByMedicineId);
            deletePatientByPersonId = conn.prepareStatement(DELETE_PATIENT_BY_PERSON_ID);
            preparedStatements.add(deletePatientByPersonId);
            deletePersonById = conn.prepareStatement(DELETE_PERSON_BY_ID);
            preparedStatements.add(deletePersonById);
            deleteStaffById = conn.prepareStatement(DELETE_STAFF_BY_ID);
            preparedStatements.add(deleteStaffById);
            deleteReceiptById = conn.prepareStatement(DELETE_RECEIPT_BY_ID);
            preparedStatements.add(deleteReceiptById);
            deleteReceiptMedicineByReceiptId = conn.prepareStatement(DELETE_RECEIPT_MEDICINE_BY_RECEIPT_ID);
            preparedStatements.add(deleteReceiptMedicineByReceiptId);
            updatePersonByPersonId = conn.prepareStatement(UPDATE_PERSON_BY_PERSON_ID);
            preparedStatements.add(updatePersonByPersonId);
            updateContactByPersonId = conn.prepareStatement(UPDATE_CONTACT_BY_PERSON_ID);
            preparedStatements.add(updateContactByPersonId);
            updateMedicineByMedicineId = conn.prepareStatement(UPDATE_MEDICINE_BY_MEDICINE_ID);
            preparedStatements.add(updateMedicineByMedicineId);
            updateMedicineStockByMedicineId = conn.prepareStatement(UPDATE_MEDICINE_STOCK_BY_MEDICINE_ID);
            preparedStatements.add(updateMedicineStockByMedicineId);
            updateStaffByStaffId = conn.prepareStatement(UPDATE_STAFF_BY_STAFF_ID);
            preparedStatements.add(updateStaffByStaffId);
            updateDoctorByStaffId = conn.prepareStatement(UPDATE_DOCTOR_BY_STAFF_ID);
            preparedStatements.add(updateDoctorByStaffId);
            updateNurseByStaffId = conn.prepareStatement(UPDATE_NURSE_BY_STAFF_ID);
            preparedStatements.add(updateNurseByStaffId);
            insertPerson = conn.prepareStatement(INSERT_PERSON);
            preparedStatements.add(insertPerson);
            insertContact = conn.prepareStatement(INSERT_CONTACT);
            preparedStatements.add(insertContact);
            insertStaff = conn.prepareStatement(INSERT_STAFF);
            preparedStatements.add(insertStaff);
            insertDoctor = conn.prepareStatement(INSERT_DOCTOR);
            preparedStatements.add(insertDoctor);
            insertNurse = conn.prepareStatement(INSERT_NURSE);
            preparedStatements.add(insertNurse);
            insertPatient = conn.prepareStatement(INSERT_PATIENT);
            preparedStatements.add(insertPatient);
            insertMedicine = conn.prepareStatement(INSERT_MEDICINE);
            preparedStatements.add(insertMedicine);
            insertMedicineStock = conn.prepareStatement(INSERT_MEDICINE_STOCK);
            preparedStatements.add(insertMedicineStock);
            insertReceipt = conn.prepareStatement(INSERT_RECEIPT);
            preparedStatements.add(insertReceipt);
            insertReceiptMedicine = conn.prepareStatement(INSERT_RECEIPT_MEDICINE);
            preparedStatements.add(insertReceiptMedicine);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {

            if (preparedStatements != null) {
                for (PreparedStatement preparedStatement : preparedStatements) {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                }
            }

            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    private int getPersonIdByStaffId(int id) {
        try {
            queryStaffById.setInt(1, id);
            ResultSet results = queryStaffById.executeQuery();
            if (results != null) {
                return results.getInt(COLUMN_STAFF_PERSON_ID);
            } else {
                throw new IllegalStateException("Unexpected value: id-> " + id);
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    private int queryLastMedicineId(){
        try(Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_LAST_MEDICINE_ID);
            if (results != null) {
                return results.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    private int queryLastReceiptId(){
        try(Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_LAST_RECEIPT_ID);
            if (results != null) {
                return results.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    private int queryLastPersonId(){
        try(Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_LAST_PERSON_ID);
            if (results != null) {
                return results.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    private int queryLastStaffId(){
        try(Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_LAST_STAFF_ID);
            if (results != null) {
                return results.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    private String getExpretiseByStaffId(int id) {
        try {
            queryDoctorExpretiseByStaffId.setInt(1, id);
            ResultSet results = queryDoctorExpretiseByStaffId.executeQuery();
            if (results != null) {
                return results.getString(COLUMN_DOCTOR_EXPRETISE);
            } else {
                throw new IllegalStateException("Unexpected value: id-> " + id);
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    private String getWorkingAreaByStaffId(int id) {
        try {
            queryNurseWorkingAreaByStaffId.setInt(1, id);
            ResultSet results = queryNurseWorkingAreaByStaffId.executeQuery();
            if (results != null) {
                return results.getString(COLUMN_NURSE_WORKING_AREA);
            } else {
                throw new IllegalStateException("Unexpected value: id-> " + id);
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * @return return null if there is an error or the username and password don't
     *         match. In success, return the status of the staff in string
     */
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

    /**
     * @return ArrayList of Recepsionist objects for information of all
     *         recepsionist.
     */
    public ArrayList<Receptionist> queryRecepsionist() {
        try (Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_RECEPSIONIST);
            ArrayList<Receptionist> recepsionists = new ArrayList<>();
            while (results.next()) {
                Receptionist receptionist = new Receptionist();
                receptionist.setId(results.getInt(COLUMN_STAFF_ID));
                receptionist.setUsername(results.getString(COLUMN_STAFF_USERNAME));
                receptionist.setPassword(results.getString(COLUMN_STAFF_PASSWORD));
                receptionist.setStatus(Status.RECEPTIONIST);
                receptionist.setName(results.getString(COLUMN_PERSON_NAME));
                receptionist.setSurname(results.getString(COLUMN_PERSON_SURNAME));
                receptionist.setAge(results.getInt(COLUMN_PERSON_AGE));
                Gender gender = results.getString(COLUMN_PERSON_GENDER).equals("MALE") ? Gender.MALE : Gender.FEMALE;
                receptionist.setGender(gender);
                Contact contact = new Contact();
                contact.setAddress(results.getString(COLUMN_CONTACT_ADDRESS));
                contact.setPhone(results.getString(COLUMN_CONTACT_PHONE));
                contact.setEmail(results.getString(COLUMN_CONTACT_EMAIL));
                receptionist.setContact(contact);
                recepsionists.add(receptionist);
            }
            return recepsionists;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * @return ArrayList of Pharmacist objects for information of all pharmacist.
     */
    public ArrayList<Pharmacist> queryPharmacists() {
        try (Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_PHARMACIST);
            ArrayList<Pharmacist> pharmacists = new ArrayList<>();
            while (results.next()) {
                Pharmacist pharmacist = new Pharmacist();
                pharmacist.setId(results.getInt(COLUMN_STAFF_ID));
                pharmacist.setUsername(results.getString(COLUMN_STAFF_USERNAME));
                pharmacist.setPassword(results.getString(COLUMN_STAFF_PASSWORD));
                pharmacist.setStatus(Status.PHARMACIST);
                pharmacist.setName(results.getString(COLUMN_PERSON_NAME));
                pharmacist.setSurname(results.getString(COLUMN_PERSON_SURNAME));
                pharmacist.setAge(results.getInt(COLUMN_PERSON_AGE));
                Gender gender = results.getString(COLUMN_PERSON_GENDER).equals("MALE") ? Gender.MALE : Gender.FEMALE;
                pharmacist.setGender(gender);
                Contact contact = new Contact();
                contact.setAddress(results.getString(COLUMN_CONTACT_ADDRESS));
                contact.setPhone(results.getString(COLUMN_CONTACT_PHONE));
                contact.setEmail(results.getString(COLUMN_CONTACT_EMAIL));
                pharmacist.setContact(contact);
                pharmacists.add(pharmacist);
            }
            return pharmacists;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * @return ArrayList of Nurse objects for information of all nurses.
     */
    public ArrayList<Nurse> queryNurse() {
        try (Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_NURSE);
            ArrayList<Nurse> nurses = new ArrayList<>();
            while (results.next()) {
                Nurse nurse = new Nurse();
                nurse.setId(results.getInt(COLUMN_STAFF_ID));
                nurse.setUsername(results.getString(COLUMN_STAFF_USERNAME));
                nurse.setPassword(results.getString(COLUMN_STAFF_PASSWORD));
                nurse.setStatus(Status.NURSE);
                nurse.setWorkingArea(results.getString(COLUMN_NURSE_WORKING_AREA));
                nurse.setName(results.getString(COLUMN_PERSON_NAME));
                nurse.setSurname(results.getString(COLUMN_PERSON_SURNAME));
                nurse.setAge(results.getInt(COLUMN_PERSON_AGE));
                nurse.setGender(results.getString(COLUMN_PERSON_GENDER).equals("MALE") ? Gender.MALE : Gender.FEMALE);
                Contact contact = new Contact();
                contact.setAddress(results.getString(COLUMN_CONTACT_ADDRESS));
                contact.setPhone(results.getString(COLUMN_CONTACT_PHONE));
                contact.setEmail(results.getString(COLUMN_CONTACT_EMAIL));
                nurse.setContact(contact);
                nurses.add(nurse);
            }
            return nurses;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * @return ArrayList of Doctor objects for information of all doctors.
     */
    public ArrayList<Doctor> queryDoctors() {
        try (Statement Statement = conn.createStatement()) {
            ResultSet results = Statement.executeQuery(QUERY_DOCTORS);
            ArrayList<Doctor> doctors = new ArrayList<>();
            while (results.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(results.getInt(COLUMN_STAFF_ID));
                doctor.setUsername(results.getString(COLUMN_STAFF_USERNAME));
                doctor.setPassword(results.getString(COLUMN_STAFF_PASSWORD));
                doctor.setStatus(Status.DOCTOR);
                doctor.setExpertise(results.getString(COLUMN_DOCTOR_EXPRETISE));
                doctor.setName(results.getString(COLUMN_PERSON_NAME));
                doctor.setSurname(results.getString(COLUMN_PERSON_SURNAME));
                doctor.setAge(results.getInt(COLUMN_PERSON_AGE));
                doctor.setGender(results.getString(COLUMN_PERSON_GENDER).equals("MALE") ? Gender.MALE : Gender.FEMALE);
                Contact contact = new Contact();
                contact.setAddress(results.getString(COLUMN_CONTACT_ADDRESS));
                contact.setPhone(results.getString(COLUMN_CONTACT_PHONE));
                contact.setEmail(results.getString(COLUMN_CONTACT_EMAIL));
                doctor.setContact(contact);
                doctors.add(doctor);
            }
            return doctors;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * @param medicine_supply the supply object to update
     */
    public void updateMedicineSupply(MedicineSupply medicine_supply) {
        try {
            ResultSet results = queryMedicine.executeQuery();
            while (results.next()) {
                Medicine medicine = new Medicine();
                medicine.setId(results.getInt(COLUMN_MEDICINE_ID));
                medicine.setName(results.getString(COLUMN_MEDICINE_NAME));
                medicine.setType(MedicineType.valueOf(results.getString(COLUMN_MEDICINE_TYPE)));
                medicine_supply.setStock(medicine, results.getInt(COLUMN_MEDICINE_STOCK_AMOUNT));
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    /**
     * @param staff_id the id of the staff
     * @return patient arraylist matching the staff id
     */
    public ArrayList<Patient> queryPatients(int staff_id) {
        try {
            queryPatientsByStaffId.setInt(1, staff_id);
            ResultSet results = queryPatientsByStaffId.executeQuery();
            ArrayList<Patient> patients = new ArrayList<>();
            while (results.next()) {
                Patient patient = new Patient();
                patient.setId(results.getInt(1));
                patient.setName(results.getString(COLUMN_PERSON_NAME));
                patient.setSurname(results.getString(COLUMN_PERSON_SURNAME));
                patient.setAge(results.getInt(COLUMN_PERSON_AGE));
                Gender gender = results.getString(COLUMN_PERSON_GENDER).equals("MALE") ? Gender.MALE : Gender.FEMALE;
                patient.setGender(gender);

                patient.setBloodType(BloodType.valueOf(results.getString(COLUMN_PATIENT_BLOOD_TYPE)));
                patient.setPriority(Priority.valueOf(results.getString(COLUMN_PATIENT_PRIORITY)));
                patient.setEmergencyState(EmergencyState.valueOf(results.getString(COLUMN_PATIENT_EMERGENCY_STATE)));

                String complaint = results.getString(COLUMN_PATIENT_COMPLAINT);
                if (complaint != null)
                    patient.setComplaint(complaint);
                else
                    patient.setComplaint(null);

                String dateString = results.getString(COLUMN_PATIENT_APPOINTMENT);
                if (dateString != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
                    patient.setAppointment(dateTime);
                } else {
                    patient.setAppointment(null);
                }
                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * @param username the username of the staff entered in the login page.
     * @return int staff id according to the username.
     */
    public int queryStaffId(String username) {
        try {
            queryStaffIdByUsername.setString(1, username);
            ResultSet results = queryStaffIdByUsername.executeQuery();
            if (results != null) {
                return results.getInt(COLUMN_STAFF_ID);
            }
            return -1;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    private String queryUsernameById(int staff_id){
        try {
            queryStaffUsernameById.setInt(1, staff_id);
            ResultSet results = queryStaffUsernameById.executeQuery();
            if (results != null) {
                return results.getString(COLUMN_STAFF_USERNAME);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * @param staff_id the id of the staff entered in the login page.
     * @return Staff object for information of the staff according to the username.
     */
    public Staff queryStaffProfile(int staff_id) {
        return queryStaffProfile(queryUsernameById(staff_id));
    }

    /**
     * @param username the username of the staff entered in the login page.
     * @return Staff object for information of the staff according to the username.
     */
    public Staff queryStaffProfile(String username) {
        try {
            queryStaffByUsername.setString(1, username);
            ResultSet results = queryStaffByUsername.executeQuery();
            if (results != null) {
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
                switch (status) {
                    case "manager":
                        staff = new Manager();
                        staff.setStatus(Status.MANAGER);
                        break;
                    case "doctor":
                        String expertise = getExpretiseByStaffId(id);
                        staff = new Doctor(expertise);
                        staff.setStatus(Status.DOCTOR);
                        break;
                    case "nurse":
                        String workingArea = getWorkingAreaByStaffId(id);
                        staff = new Nurse(workingArea);
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
                        throw new IllegalStateException("Unknown value: " + username);
                }
                staff.setAge(age);
                staff.setGender(gender);
                staff.setContact(contact);
                staff.setName(name);
                staff.setSurname(surname);
                staff.setId(id);
                return staff;
            } else {
                throw new IllegalStateException("Unexpected value: username-> " + username);
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * updates only personal information of the staff.
     * 
     * @param staff a new staff object to update existing staff by id.
     * @return message that shows the result of the operation.
     */
    public String updateStaffProfile(Staff staff) {

        try {
            conn.setAutoCommit(false);

            int personId = getPersonIdByStaffId(staff.getId());
            if (personId == -1) {
                return "Couldn't find the staff by id: " + staff.getId();
            } else {
                staff.setId(personId);
                Boolean affectedRows1 = updatePerson(staff);
                Boolean affectedRows2 = updateContact(personId, staff.getContact());
                if (affectedRows1 && affectedRows2) {
                    conn.commit();
                    return "Staff updated successfully";
                } else {
                    return "Couldn't update the staff";
                }
            }

            // if any error occurs, sql will rollback in the catch block
        } catch (Exception e) {
            System.out.println("Update staff failed: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
                return "Update staff failed: " + e.getMessage();
            } catch (SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
                return "Update staff failed: " + e2.getMessage();
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }

    /**
     * updates personal information as well as username,password of the staff.
     * 
     * @param staff a new staff object to update existing staff by id.
     * @return message that shows the result of the operation.
     */
    public String updateStaffUser(Staff staff) {

        try {
            conn.setAutoCommit(false);

            int personId = getPersonIdByStaffId(staff.getId());
            if (personId == -1) {
                return "Couldn't find the staff by id: " + staff.getId();
            } else {
                ArrayList<Boolean> affectedRows = new ArrayList<>();
                affectedRows.add(updateStaff(staff));
                if (staff.getStatus() == Status.DOCTOR)
                    affectedRows.add(updateDoctor((Doctor) staff));
                else if (staff.getStatus() == Status.NURSE)
                    affectedRows.add(updateNurse((Nurse) staff));
                staff.setId(personId);
                affectedRows.add(updatePerson(staff));
                affectedRows.add(updateContact(personId, staff.getContact()));
                if (!affectedRows.contains(false)) {
                    conn.commit();
                    return "Staff updated successfully";
                } else {
                    return "Couldn't update the staff";
                }
            }

            // if any error occurs, sql will rollback in the catch block
        } catch (Exception e) {
            System.out.println("Update staff failed: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
                return "Update staff failed: " + e.getMessage();
            } catch (SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
                return "Update staff failed: " + e2.getMessage();
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }

    private boolean deleteContact(int personId) {
        try {
            deleteContactByPersonId.setInt(1, personId);
            int affectedRows = deleteContactByPersonId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete contact failed: " + e.getMessage());
            return false;
        }
    }

    private boolean deletePerson(int personId) {
        try {
            deletePersonById.setInt(1, personId);
            int affectedRows = deletePersonById.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete person failed: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteStaff(int staffId) {
        try {
            deleteStaffById.setInt(1, staffId);
            int affectedRows = deleteStaffById.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete staff failed: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteDoctor(int staffId) {
        try {
            deleteDoctorByStaffId.setInt(1, staffId);
            int affectedRows = deleteDoctorByStaffId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete doctor failed: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteNurse(int staffId) {
        try {
            deleteNurseByStaffId.setInt(1, staffId);
            int affectedRows = deleteNurseByStaffId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete nurse failed: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteMedicine(int medicineId) {
        try {
            deleteMedicineById.setInt(1, medicineId);
            int affectedRows = deleteMedicineById.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete medicine failed: " + e.getMessage());
            return false;
        }
    }

    private boolean deletePatient(int personId) {
        try {
            deletePatientByPersonId.setInt(1, personId);
            int affectedRows = deletePatientByPersonId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete patient failed: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteMedicineStock(int medicineId) {
        try {
            deleteMedicineStockByMedicineId.setInt(1, medicineId);
            int affectedRows = deleteMedicineStockByMedicineId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete medicine stock failed: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteReceipt(int receiptId) {
        try {
            deleteReceiptById.setInt(1, receiptId);
            int affectedRows = deleteReceiptById.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete receipt failed: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteReceiptMedicine(int receiptId) {
        try {
            deleteReceiptMedicineByReceiptId.setInt(1, receiptId);
            int affectedRows = deleteReceiptMedicineByReceiptId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Delete receipt medicine failed: " + e.getMessage());
            return false;
        }
    }

    private boolean updatePerson(Person person) {
        try {
            updatePersonByPersonId.setString(1, person.getName());
            updatePersonByPersonId.setString(2, person.getSurname());
            updatePersonByPersonId.setString(3, person.getGender().name());
            updatePersonByPersonId.setInt(4, person.getAge());
            updatePersonByPersonId.setInt(5, person.getId());
            int affectedRows = updatePersonByPersonId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Update person failed: " + e.getMessage());
            return false;
        }
    }

    private boolean updateContact(int person_id, Contact contact) {
        try {
            updateContactByPersonId.setString(1, contact.getPhone());
            updateContactByPersonId.setString(2, contact.getEmail());
            updateContactByPersonId.setString(3, contact.getAddress());
            updateContactByPersonId.setInt(4, person_id);
            int affectedRows = updateContactByPersonId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Update contact failed: " + e.getMessage());
            return false;
        }
    }

    private boolean updateStaff(Staff staff) {
        try {
            updateStaffByStaffId.setString(1, staff.getUsername());
            updateStaffByStaffId.setString(2, staff.getPassword());
            updateStaffByStaffId.setInt(3, staff.getId());
            int affectedRows = updateStaffByStaffId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Update staff failed: " + e.getMessage());
            return false;
        }
    }

    private boolean updateDoctor(Doctor doctor) {
        try {
            updateDoctorByStaffId.setString(1, doctor.getExpertise());
            updateDoctorByStaffId.setInt(2, doctor.getId());
            int affectedRows = updateDoctorByStaffId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Update doctor failed: " + e.getMessage());
            return false;
        }
    }

    private boolean updateNurse(Nurse nurse) {
        try {
            updateNurseByStaffId.setString(1, nurse.getWorkingArea());
            updateNurseByStaffId.setInt(2, nurse.getId());
            int affectedRows = updateNurseByStaffId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Update nurse failed: " + e.getMessage());
            return false;
        }
    }

    public Boolean updateMedicine(Medicine medicine) {
        try {
            conn.setAutoCommit(false);
            updateMedicineByMedicineId.setString(1, medicine.getName());
            updateMedicineByMedicineId.setString(2, medicine.getType().name());
            updateMedicineByMedicineId.setInt(3, medicine.getId());
            int affectedRows = updateMedicineByMedicineId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }

            // if any error occurs, sql will rollback in the catch block
        } catch (Exception e) {
            System.out.println("Update staff failed: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
                System.out.println("Update staff failed: " + e.getMessage());
            } catch (SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
                System.out.println("Update staff failed: " + e2.getMessage());
            }
            return false;
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }

    public boolean updateMedicineStock(int medicineId, int quantity) {
        try {
            conn.setAutoCommit(false);
            updateMedicineStockByMedicineId.setInt(1, quantity);
            updateMedicineStockByMedicineId.setInt(2, medicineId);
            int affectedRows = updateMedicineStockByMedicineId.executeUpdate();
            if (affectedRows == 1) {
                return true;
            } else {
                return false;
            }

            // if any error occurs, sql will rollback in the catch block
        } catch (Exception e) {
            System.out.println("Update staff failed: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
                System.out.println("Update staff failed: " + e.getMessage());
            } catch (SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
                System.out.println("Update staff failed: " + e2.getMessage());
            }
            return false;
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }

    private int insertPerson(Person person) {
        try {
            ResultSet generatedKeys = insertPerson.getGeneratedKeys();
            if (!generatedKeys.next())
                throw new SQLException("Couldn't get id for person!");
            int id = generatedKeys.getInt(1);
            insertPerson.setInt(1, id);
            insertPerson.setString(2, person.getName());
            insertPerson.setString(3, person.getSurname());
            insertPerson.setString(4, person.getGender().name());
            insertPerson.setInt(5, person.getAge());
            int affectedRows = insertPerson.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert person!");
            }
            return id;
        } catch (SQLException e) {
            System.out.println("Insert person failed: " + e.getMessage());
            return -1;
        }
    }

    private boolean insertContact(int id, Contact contact) {
        try {
            insertContact.setInt(1, id);

            String phone = contact.getPhone();
            if (phone == null)
                phone = "";
            insertContact.setString(2, phone);

            String email = contact.getEmail();
            if (email == null)
                email = "";
            insertContact.setString(3, email);

            String address = contact.getAddress();
            if (address == null)
                address = "";
            insertContact.setString(4, address);

            int affectedRows = insertContact.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert contact!");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Insert contact failed: " + e.getMessage());
            return false;
        }
    }

    private int insertStaff(int person_id, Staff staff) {
        try {
            ResultSet generatedKeys = insertStaff.getGeneratedKeys();
            if (!generatedKeys.next())
                throw new SQLException("Couldn't get id for staff!");
            int id = generatedKeys.getInt(1);
            insertStaff.setInt(1, id);
            insertStaff.setString(2, staff.getUsername());
            insertStaff.setString(3, staff.getPassword());
            insertStaff.setString(4, staff.getStatus().name());
            insertStaff.setInt(5, person_id);
            int affectedRows = insertStaff.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert staff!");
            }
            return id;
        } catch (SQLException e) {
            System.out.println("Insert staff failed: " + e.getMessage());
            return -1;
        }
    }

    private boolean insertDoctor(int staff_id, Doctor doctor) {
        try {
            insertDoctor.setInt(1, staff_id);
            String expertise = doctor.getExpertise();
            if (expertise == null)
                expertise = "";
            insertDoctor.setString(2, expertise);
            int affectedRows = insertDoctor.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert doctor!");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Insert doctor failed: " + e.getMessage());
            return false;
        }
    }

    private boolean insertNurse(int staff_id, Nurse nurse) {
        try {
            insertNurse.setInt(1, staff_id);
            String workingArea = nurse.getWorkingArea();
            if (workingArea == null)
                workingArea = "";
            insertNurse.setString(2, workingArea);
            int affectedRows = insertNurse.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert nurse!");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Insert nurse failed: " + e.getMessage());
            return false;
        }
    }

    private boolean insertPatient(int person_id, int staff_id, int receipt_id, Patient patient) {
        try {

            insertPatient.setInt(1, person_id);
            if (staff_id == -1)
                insertPatient.setNull(2, java.sql.Types.INTEGER);
            else
                insertPatient.setInt(2, staff_id);

            if (receipt_id == -1)
                insertPatient.setNull(3, java.sql.Types.INTEGER);
            else
                insertPatient.setInt(3, receipt_id);

            String complaint = patient.getComplaint();
            if (complaint == null)
                complaint = "";
            insertContact.setString(4, complaint);

            LocalDateTime appointment = patient.getAppointment();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = appointment.format(formatter);
            insertPatient.setString(5, formattedDateTime);

            insertContact.setString(6, patient.getEmergencyState().name());
            insertContact.setString(7, patient.getPriority().name());
            insertContact.setString(8, patient.getBloodType().name());

            int affectedRows = insertPatient.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert patient!");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Insert patient failed: " + e.getMessage());
            return false;
        }
    }

    private int insertReceipt(Receipt receipt) {
        try {
            int receipt_id = queryLastReceiptId();
            if (receipt_id == -1)
                throw new SQLException("Couldn't get id for receipt!");
            receipt_id++;

            insertReceipt.setInt(1, receipt_id);
            insertReceipt.setInt(2, receipt.getPatientId());
            insertReceipt.setInt(3, receipt.getStaffId());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date givenDate = receipt.getGivenDate();
            String formattedgivenDate = formatter.format(givenDate);
            insertReceipt.setString(4, formattedgivenDate);

            Date expireDate = receipt.getExpireDate();
            String formattedexpireDate = formatter.format(expireDate);
            insertReceipt.setString(5, formattedexpireDate);

            Boolean isGiven = receipt.isGiven();
            insertReceipt.setString(6, isGiven.toString());

            int affectedRows = insertReceipt.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert receipt!");
            }
            return receipt_id;
        } catch (SQLException e) {
            System.out.println("Insert receipt failed: " + e.getMessage());
            return -1;
        }
    }

    private boolean insertReceiptMedicine(int receipt_id, int medicine_id, int quantity) {
        try {
            insertReceiptMedicine.setInt(1, receipt_id);
            insertReceiptMedicine.setInt(2, medicine_id);
            insertReceiptMedicine.setInt(3, quantity);
            int affectedRows = insertReceiptMedicine.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert receipt medicine!");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Insert receipt medicine failed: " + e.getMessage());
            return false;
        }
    }

    private int insertMedicine(Medicine medicine) {
        try {
            int medicine_id = queryLastMedicineId();
            if (medicine_id == -1)
                throw new SQLException("Couldn't get id for medicine!");
            medicine_id++;

            insertMedicine.setInt(1, medicine_id);
            insertMedicine.setString(2, medicine.getName());
            insertMedicine.setString(3, medicine.getType().name());

            int affectedRows = insertMedicine.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert medicine!");
            }
            return medicine_id;
        } catch (SQLException e) {
            System.out.println("Insert medicine failed: " + e.getMessage());
            return -1;
        }
    }

    private boolean insertMedicineStock(int medicine_id, int medicine_amount) {
        try {
            if (medicine_amount <= 0)
                throw new SQLException("Medicine amount can't be negative!");
            insertMedicineStock.setInt(1, medicine_id);
            insertMedicineStock.setInt(2, medicine_amount);
            int affectedRows = insertMedicineStock.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert medicine stock!");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Insert medicine stock failed: " + e.getMessage());
            return false;
        }
    }

    public String addNewMedicine(Medicine medicine, int amount) {
        try {
            conn.setAutoCommit(false);
            ArrayList<Boolean> results = new ArrayList<>();
            int medicine_id = insertMedicine(medicine);
            if (medicine_id > 0)
                results.add(true);
            else
                results.add(false);

            Boolean medicineStockResult = insertMedicineStock(medicine_id, amount);
            results.add(medicineStockResult);

            if (results.contains(false)) {
                throw new SQLException("Couldn't insert medicine!");
            }
            conn.commit();
            return "Medicine added successfully!";
            // if any error occurs, sql will rollback in the catch block
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Adding new medicine failed: " + e.getMessage());
            try {
                sb.append("Performing rollback");
                conn.rollback();
                sb.append("Adding new medicine failed: " + e.getMessage());
            } catch (SQLException e2) {
                sb.append("Oh boy! Things are really bad! " + e2.getMessage());
                sb.append("Adding new medicine failed: " + e2.getMessage());
            }
            return sb.toString();
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }

    public String addNewReceipt(Receipt receipt) {
        try {
            conn.setAutoCommit(false);
            ArrayList<Boolean> results = new ArrayList<>();
            int receipt_id = insertReceipt(receipt);
            if (receipt_id > 0)
                results.add(true);
            else
                results.add(false);

            TreeMap<Medicine, Receipt.ReceiptItem> medicines = receipt.getContent();
            for (Map.Entry<Medicine, Receipt.ReceiptItem> entry : medicines.entrySet()) {
                int medicine_id = entry.getKey().getId();
                int quantity = entry.getValue().getAmount();
                Boolean receiptMedicineResult = insertReceiptMedicine(receipt_id, medicine_id, quantity);
                results.add(receiptMedicineResult);
            }

            if (results.contains(false)) {
                throw new SQLException("Couldn't insert receipt!");
            }
            conn.commit();
            return "Receipt added successfully!";
            // if any error occurs, sql will rollback in the catch block
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Adding new receipt failed: " + e.getMessage());
            try {
                sb.append("Performing rollback");
                conn.rollback();
                sb.append("Adding new receipt failed: " + e.getMessage());
            } catch (SQLException e2) {
                sb.append("Oh boy! Things are really bad! " + e2.getMessage());
                sb.append("Adding new receipt failed: " + e2.getMessage());
            }
            return sb.toString();
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }

}