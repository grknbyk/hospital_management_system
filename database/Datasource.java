package database;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import model.Contact;
import model.Doctor;
import model.Manager;
import model.Medicine;
import model.MedicineSupply;
import model.MedicineSupply.SupplyItem;
import model.Nurse;
import model.Patient;
import model.Person;
import model.Pharmacist;
import model.Receipt;
import model.Receptionist;
import model.Staff;
import model.enums.BloodType;
import model.enums.EmergencyState;
import model.enums.Expertise;
import model.enums.Gender;
import model.enums.MedicineType;
import model.enums.Priority;
import model.enums.Status;

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
    private static final String COLUMN_PATIENT_PERSON_ID = "person_id";
    private static final String COLUMN_PATIENT_STAFF_ID = "staff_id";
    private static final String COLUMN_PATIENT_RECEIPT_ID = "receipt_id";
    private static final String COLUMN_PATIENT_COMPLAINT = "complaint";
    private static final String COLUMN_PATIENT_APPOINTMENT = "appointment";
    private static final String COLUMN_PATIENT_EMERGENCY_STATE = "emergency_state";
    private static final String COLUMN_PATIENT_PRIORITY = "priority";
    private static final String COLUMN_PATIENT_BLOOD_TYPE = "blood_type";
    private static final String COLUMN_PATIENT_DELETION_STATUS = "deletion_status";

    private static final String TABLE_DOCTOR = "doctor";
    private static final String COLUMN_DOCTOR_STAFF_ID = "staff_id";
    private static final String COLUMN_DOCTOR_EXPERTISE = "expertise";

    private static final String TABLE_NURSE = "nurse";
    private static final String COLUMN_NURSE_STAFF_ID = "staff_id";
    private static final String COLUMN_NURSE_WORKING_AREA = "working_area";

    private static final String TABLE_RECEIPT = "receipt";
    private static final String COLUMN_RECEIPT_ID = "id";
    private static final String COLUMN_RECEIPT_PATIENT_ID = "patient_id";
    private static final String COLUMN_RECEIPT_STAFF_ID = "staff_id";
    private static final String COLUMN_RECEIPT_GIVEN_DATE = "given_date";
    private static final String COLUMN_RECEIPT_EXPIRE_DATE = "expire_date";
    private static final String COLUMN_RECEIPT_IS_GIVEN = "is_given";

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

    private static final String QUERY_STAFF_USERNAME_BY_ID = "SELECT " + COLUMN_STAFF_USERNAME + " FROM " + TABLE_STAFF
            +
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
            TABLE_DOCTOR + "." + COLUMN_DOCTOR_EXPERTISE + ", " +
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

    private static final String QUERY_DOCTOR_EXPRETISE_BY_STAFF_ID = " SELECT " + COLUMN_DOCTOR_EXPERTISE +
            " FROM " + TABLE_DOCTOR +
            " WHERE " + COLUMN_DOCTOR_STAFF_ID + " = ?";

    private static final String QUERY_NURSE_WORKING_AREA_BY_STAFF_ID = " SELECT " + COLUMN_NURSE_WORKING_AREA +
            " FROM " + TABLE_NURSE +
            " WHERE " + COLUMN_NURSE_STAFF_ID + " = ?";

    private static final String QUERY_PATIENTS_BY_STAFF_ID = " SELECT * FROM " + TABLE_PATIENT +
            " INNER JOIN " + TABLE_PERSON +
            " ON " + TABLE_PATIENT + "." + COLUMN_PATIENT_PERSON_ID + " = " + TABLE_PERSON + "." + COLUMN_PERSON_ID +
            " WHERE " + COLUMN_PATIENT_STAFF_ID + " = ?";

    private static final String QUERY_RECEIPTS = " SELECT * FROM " + TABLE_RECEIPT;

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
            COLUMN_STAFF_PASSWORD + " = ?, " +
            COLUMN_STAFF_STATUS + " = ? " +
            " WHERE " + COLUMN_STAFF_ID + " = ?";

    private static final String UPDATE_DOCTOR_BY_STAFF_ID = "UPDATE " + TABLE_DOCTOR + " SET " +
            COLUMN_DOCTOR_EXPERTISE + " = ? " +
            " WHERE " + COLUMN_DOCTOR_STAFF_ID + " = ?";

    private static final String UPDATE_NURSE_BY_STAFF_ID = "UPDATE " + TABLE_NURSE + " SET " +
            COLUMN_NURSE_WORKING_AREA + " = ? " +
            " WHERE " + COLUMN_NURSE_STAFF_ID + " = ?";

    private static final String INSERT_PERSON = "INSERT INTO " + TABLE_PERSON +
            " VALUES (?,?,?,?,?)";

    private static final String INSERT_CONTACT = "INSERT INTO " + TABLE_CONTACT +
            " VALUES (?,?,?,?)";

    private static final String INSERT_STAFF = "INSERT INTO " + TABLE_STAFF +
            " VALUES (?,?,?,?,?)";

    private static final String INSERT_DOCTOR = "INSERT INTO " + TABLE_DOCTOR +
            " VALUES (?,?)";

    private static final String INSERT_NURSE = "INSERT INTO " + TABLE_NURSE +
            " VALUES (?,?)";

    private static final String QUERY_DOCTORS_BY_EXPERTISE = "SELECT " + COLUMN_DOCTOR_STAFF_ID + " FROM "
            + TABLE_DOCTOR +
            " WHERE " + COLUMN_DOCTOR_EXPERTISE + " = ?";

    private static final String INSERT_PATIENT = "INSERT INTO " + TABLE_PATIENT +
            " VALUES (?,?,?,?,?,?,?,?,\"not deleted\")";

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
    private static final String QUERY_LAST_MEDICINE_ID = "SELECT MAX(" + COLUMN_MEDICINE_ID + ") FROM "
            + TABLE_MEDICINE;
    private static final String QUERY_LAST_RECEIPT_ID = "SELECT MAX(" + COLUMN_RECEIPT_ID + ") FROM " + TABLE_RECEIPT;

    private static final String QUERY_PATIENTS_NULL_STAFF = "SELECT * FROM " + TABLE_PATIENT +
            " WHERE ( " + COLUMN_PATIENT_STAFF_ID + " IS NULL OR " + COLUMN_PATIENT_STAFF_ID + " = -1 )" + " AND "
            + COLUMN_PATIENT_DELETION_STATUS + " = \"not deleted\"";

    private static final String QUERY_MEDICINE_ID_BY_NAME = "SELECT " + COLUMN_MEDICINE_ID + " FROM " + TABLE_MEDICINE
            + " WHERE " + COLUMN_MEDICINE_NAME + " = ?";

    private static final String QUERY_MEDICINE_BY_RECEIPT_ID = "SELECT " +
            TABLE_MEDICINE + "." + COLUMN_MEDICINE_ID + ", " +
            TABLE_MEDICINE + "." + COLUMN_MEDICINE_NAME + ", " +
            TABLE_MEDICINE + "." + COLUMN_MEDICINE_TYPE + ", " +
            TABLE_RECEIPT_MEDICINE + "." + COLUMN_RECEIPT_MEDICINE_AMOUNT + " " +
            " FROM " + TABLE_RECEIPT_MEDICINE +
            " INNER JOIN " + TABLE_MEDICINE +
            " ON " + TABLE_RECEIPT_MEDICINE + "." + COLUMN_RECEIPT_MEDICINE_MEDICINE_ID + " = " +
            TABLE_MEDICINE + "." + COLUMN_MEDICINE_ID +
            " WHERE " + TABLE_RECEIPT_MEDICINE + "." + COLUMN_RECEIPT_MEDICINE_RECEIPT_ID + " = ?";

    private static final String QUERY_PERSON_BY_ID = "SELECT * FROM " + TABLE_PERSON +
            " WHERE " + COLUMN_PERSON_ID + " = ?";

    private static final String QUERY_MEDICINE2 = "SELECT * FROM " + TABLE_MEDICINE;

    private static final String QUERY_MEDICINE_BY_NAME = "SELECT * FROM " + TABLE_MEDICINE +
            " WHERE " + COLUMN_MEDICINE_NAME + " = ?";

    private static final String UPDATE_RECEIPT_GIVEN = "UPDATE " + TABLE_RECEIPT + " SET " +
            COLUMN_RECEIPT_IS_GIVEN + " = ? " +
            " WHERE " + COLUMN_RECEIPT_ID + " = ?";

    private static final String SET_DELETED_PATIENT = "UPDATE " + TABLE_PATIENT + " SET " +
            COLUMN_PATIENT_DELETION_STATUS + " = \"deleted\" " +
            " WHERE " + COLUMN_PATIENT_PERSON_ID + " = ?";

    private static final String UPDATE_PATIENT = "UPDATE " + TABLE_PATIENT + " SET " +
            COLUMN_PATIENT_STAFF_ID + " = ?, " +
            COLUMN_PATIENT_COMPLAINT + " = ?, " +
            COLUMN_PATIENT_APPOINTMENT + " = ? " +
            " WHERE " + COLUMN_PATIENT_PERSON_ID + " = ?";

    private Connection conn;

    private ArrayList<PreparedStatement> preparedStatements;
    private PreparedStatement queryLogin;
    private PreparedStatement queryStaffByUsername;
    private PreparedStatement queryStaffUsernameById;
    private PreparedStatement queryMedicineIdByName;
    private PreparedStatement queryStaffIdByUsername;
    private PreparedStatement queryPatientsByStaffId;
    private PreparedStatement queryMedicine;
    private PreparedStatement queryPersonById;
    private PreparedStatement queryMedicineByName;
    private PreparedStatement queryMedicineByReceiptId;
    private PreparedStatement queryStaffById;
    private PreparedStatement queryDoctorExpretiseByStaffId;
    private PreparedStatement queryNurseWorkingAreaByStaffId;
    private PreparedStatement queryDoctorsByExpertise;
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
    private PreparedStatement updatePatientByPersonId;
    private PreparedStatement updateReceiptIsGiven;
    private PreparedStatement updatePatientSetDeleted;
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
            queryMedicineIdByName = conn.prepareStatement(QUERY_MEDICINE_ID_BY_NAME);
            preparedStatements.add(queryMedicineIdByName);
            updatePatientByPersonId = conn.prepareStatement(UPDATE_PATIENT);
            preparedStatements.add(updatePatientByPersonId);
            queryDoctorsByExpertise = conn.prepareStatement(QUERY_DOCTORS_BY_EXPERTISE);
            preparedStatements.add(queryDoctorsByExpertise);
            updateReceiptIsGiven = conn.prepareStatement(UPDATE_RECEIPT_GIVEN);
            preparedStatements.add(updateReceiptIsGiven);
            queryMedicineByName = conn.prepareStatement(QUERY_MEDICINE_BY_NAME);
            preparedStatements.add(queryMedicineByName);
            updatePatientSetDeleted = conn.prepareStatement(SET_DELETED_PATIENT);
            preparedStatements.add(updatePatientSetDeleted);
            queryPersonById = conn.prepareStatement(QUERY_PERSON_BY_ID);
            preparedStatements.add(queryPersonById);
            queryMedicineByReceiptId = conn.prepareStatement(QUERY_MEDICINE_BY_RECEIPT_ID);
            preparedStatements.add(queryMedicineByReceiptId);
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

    public ArrayList<Person> queryDoctors(Expertise expertise){
        try{
            ArrayList<Person> doctors = new ArrayList<>();
            queryDoctorsByExpertise.setString(1, expertise.getValue());
            ResultSet results = queryDoctorsByExpertise.executeQuery();
            ArrayList<Integer> staffIds = new ArrayList<>();
            while(results.next())
                staffIds.add(results.getInt(COLUMN_DOCTOR_STAFF_ID));

            for(int staffId : staffIds){
                int person_id = getPersonIdByStaffId(staffId);
                Person person = queryPersonbyId(person_id);
                person.setId(staffId);
                doctors.add(person);
            }            
            return doctors;
        }catch(SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
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

    private int queryLastMedicineId() {
        try (Statement statement = conn.createStatement()) {
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

    private int queryLastReceiptId() {
        try (Statement statement = conn.createStatement()) {
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

    private int queryLastPersonId() {
        try (Statement statement = conn.createStatement()) {
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

    public void deletePatientById(int patient_id) {
        try {
            conn.setAutoCommit(false);
            updatePatientSetDeleted.setInt(1, patient_id);
            int result = updatePatientSetDeleted.executeUpdate();
            if (result == 1) {
                conn.commit();
            } else {
                throw new SQLException("Patient was not deleted");
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
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }

    public void updateReceipIsGiven(int receipt_id, boolean isGiven) {
        try {
            updateReceiptIsGiven.setString(1, (isGiven ? "true" : "false"));
            updateReceiptIsGiven.setInt(2, receipt_id);
            updateReceiptIsGiven.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    private int queryLastStaffId() {
        try (Statement statement = conn.createStatement()) {
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
                return results.getString(COLUMN_DOCTOR_EXPERTISE);
            } else {
                throw new IllegalStateException("Unexpected value: id-> " + id);
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public Person queryPersonbyId(int person_id) {
        try {
            queryPersonById.setInt(1, person_id);
            ResultSet results = queryPersonById.executeQuery();
            return new Person() {
                {
                    setId(person_id);
                    setName(results.getString(COLUMN_PERSON_NAME));
                    setSurname(results.getString(COLUMN_PERSON_SURNAME));
                    setAge(results.getInt(COLUMN_PERSON_AGE));
                    setGender(Gender.valueOf(results.getString(COLUMN_PERSON_GENDER)));
                }
            };

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
     * @return ArrayList of Medicine objects relevant to receipt_id
     * @param receipt_id the id of the receipt
     */
    public HashMap<Medicine, Integer> queryMedicineByReceiptId(int receipt_id) {
        try {
            queryMedicineByReceiptId.setInt(1, receipt_id);
            ResultSet results = queryMedicineByReceiptId.executeQuery();
            HashMap<Medicine, Integer> medicines = new HashMap<>();
            while (results.next()) {
                int medicine_id = results.getInt(1);
                String medicine_name = results.getString(2);
                MedicineType medicine_type = MedicineType.valueOf(results.getString(3));
                int medicine_amount = results.getInt(4);
                Medicine medicine = new Medicine(medicine_name, medicine_type);
                medicine.setId(medicine_id);
                medicines.put(medicine, medicine_amount);
            }
            return medicines;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * @return ArrayList of Receipts objects for information of all
     *         receipts.
     * @throws ParseException
     */
    public ArrayList<Receipt> queryReceipts() {
        try (Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_RECEIPTS);
            ArrayList<Receipt> receipts = new ArrayList<>();
            while (results.next()) {
                String givenDateString = results.getString(COLUMN_RECEIPT_GIVEN_DATE);
                String expireDateString = results.getString(COLUMN_RECEIPT_EXPIRE_DATE);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date givenDate = null;
                Date expireDate = null;
                try {
                    java.util.Date date1 = dateFormat.parse(givenDateString);
                    java.util.Date date2 = dateFormat.parse(expireDateString);
                    givenDate = new Date(date1.getTime());
                    expireDate = new Date(date2.getTime());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                int receipt_id = results.getInt(COLUMN_RECEIPT_ID);
                int staff_id = results.getInt(COLUMN_RECEIPT_STAFF_ID);
                int patient_id = results.getInt(COLUMN_RECEIPT_PATIENT_ID);

                Receipt receipt = new Receipt(receipt_id, patient_id, staff_id, givenDate, expireDate);
                Boolean isGiven = results.getString(COLUMN_RECEIPT_IS_GIVEN).equalsIgnoreCase("true") ? true : false;
                receipt.setGiven(isGiven);

                if (!isGiven) {
                    HashMap<Medicine, Integer> medicines = queryMedicineByReceiptId(receipt_id);
                    for (Medicine medicine : medicines.keySet()) {
                        receipt.add(medicine, medicines.get(medicine));
                    }
                }
                receipts.add(receipt);
            }
            return receipts;
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

    private Medicine queryMedicineByName(String medName) {
        try {
            queryMedicineByName.setString(1, medName);
            ResultSet results = queryMedicineByName.executeQuery();
            if (results.next()) {
                int medicine_id = results.getInt(1);
                String medicine_name = results.getString(2);
                MedicineType medicine_type = MedicineType.valueOf(results.getString(3));
                Medicine medicine = new Medicine(medicine_name, medicine_type);
                medicine.setId(medicine_id);
                return medicine;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public int queryMedicineIdByName(String medName) {
        try {
            queryMedicineByName.setString(1, medName);
            ResultSet results = queryMedicineByName.executeQuery();
            if (results.next()) {
                int medicine_id = results.getInt(1);
                return medicine_id;
            }
            return -1;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    public ArrayList<Patient> queryPatientsNullStaff() {
        try (Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_PATIENTS_NULL_STAFF);
            ArrayList<Patient> patients = new ArrayList<>();
            while (results.next()) {
                Person person = queryPersonbyId(results.getInt(COLUMN_PATIENT_PERSON_ID));
                Patient patient = new Patient();
                patient.setId(results.getInt(COLUMN_PATIENT_PERSON_ID));
                patient.setReceiptId(results.getInt(COLUMN_PATIENT_RECEIPT_ID));
                patient.setComplaint(results.getString(COLUMN_PATIENT_COMPLAINT));
                String dateString = results.getString(COLUMN_PATIENT_APPOINTMENT);
                if (dateString != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
                    patient.setAppointment(dateTime);
                }
                patient.setId(person.getId());
                patient.setName(person.getName());
                patient.setSurname(person.getSurname());
                patient.setAge(person.getAge());
                patient.setGender(person.getGender());
                patient.setDoctorId(-1);
                patient.setBloodType(BloodType.valueOf(results.getString(COLUMN_PATIENT_BLOOD_TYPE)));
                patient.setEmergencyState(EmergencyState.valueOf(results.getString(COLUMN_PATIENT_EMERGENCY_STATE)));
                patient.setPriority(Priority.valueOf(results.getString(COLUMN_PATIENT_PRIORITY)));
                patients.add(patient);
            }
            return patients;
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
     * @return ArrayList of Medicine objects for information of all medicines.
     */
    public ArrayList<Medicine> queryMedicine() {
        try (Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_MEDICINE2);
            ArrayList<Medicine> medicines = new ArrayList<>();
            while (results.next()) {
                Medicine medicine = new Medicine();
                medicine.setId(results.getInt(COLUMN_MEDICINE_ID));
                medicine.setName(results.getString(COLUMN_MEDICINE_NAME));
                medicine.setType(MedicineType.valueOf(results.getString(COLUMN_MEDICINE_TYPE)));
                medicines.add(medicine);
            }
            return medicines;
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
                doctor.setExpertise(results.getString(COLUMN_DOCTOR_EXPERTISE));
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
     * @param medicine_supply the supply object to fill from database
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
     * @param medicine_supply the supply object to update and insert records
     */
    public void saveMedicineSupply(MedicineSupply medicine_supply) {
        ArrayList<MedicineSupply.SupplyItem> items = (ArrayList<SupplyItem>) Arrays
                .stream(MedicineSupply.getInstance().toList().toArray()).map(obj -> (MedicineSupply.SupplyItem) obj)
                .collect(Collectors.toList());
        for (MedicineSupply.SupplyItem item : items) {
            Medicine medicine = queryMedicineByName(item.getMedicine().getName());
            if (medicine == null) {
                addNewMedicine(item.getMedicine(), item.getStock());
            } else {
                updateMedicineStock(item.getId(), item.getStock());
            }
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
                Object doctorId = results.getInt(COLUMN_PATIENT_STAFF_ID);
                patient.setDoctorId(doctorId == null ? -1 : (int) doctorId);
                Object receiptId = results.getInt(COLUMN_PATIENT_RECEIPT_ID);
                patient.setReceiptId(receiptId == null ? -1 : (int) receiptId);
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

    private String queryUsernameById(int staff_id) {
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
     * updates username,password, optionally workingarea or expretise of the staff.
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
                affectedRows.add(updateStaffById(staff));
                if (staff.getStatus() == Status.DOCTOR)
                    affectedRows.add(updateDoctor((Doctor) staff));
                else if (staff.getStatus() == Status.NURSE)
                    affectedRows.add(updateNurse((Nurse) staff));
                staff.setId(personId);
                if (!affectedRows.contains(false)) {
                    conn.commit();
                    return "Staff updated successfully";
                } else {
                    return "Couldn't update the staff completely";
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

    public void updateStaff(Staff staff) {
        try {
            updateStaffById(staff);
            if (staff instanceof Doctor) {
                updateDoctor((Doctor) staff);
            } else if (staff instanceof Nurse) {
                updateNurse((Nurse) staff);
            }
        } catch (Exception e) {
            System.out.println("Update staff failed: " + e.getMessage());
        }
    }

    private boolean updateStaffById(Staff staff) {
        try {
            updateStaffByStaffId.setString(1, staff.getUsername());
            updateStaffByStaffId.setString(2, staff.getPassword());
            updateStaffByStaffId.setString(3, staff.getStatus().name().toLowerCase());
            updateStaffByStaffId.setInt(4, staff.getId());
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

    private boolean updateMedicineStock(int medicineId, int quantity) {
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
            int person_id = queryLastPersonId();
            if (person_id == -1)
                throw new SQLException("Couldn't get id for person!");
            person_id++;
            insertPerson.setInt(1, person_id);
            insertPerson.setString(2, person.getName());
            insertPerson.setString(3, person.getSurname());
            insertPerson.setString(4, person.getGender().name());
            insertPerson.setInt(5, person.getAge());
            int affectedRows = insertPerson.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert person!");
            }
            return person_id;
        } catch (SQLException e) {
            System.out.println("Insert person failed: " + e.getMessage());
            return -1;
        }
    }

    private boolean insertContact(int staff_id, Contact contact) {
        try {
            insertContact.setInt(1, staff_id);

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
            int staff_id = queryLastStaffId();
            if (staff_id == -1)
                throw new SQLException("Couldn't get id for staff!");
            staff_id++;
            insertStaff.setInt(1, staff_id);
            insertStaff.setString(2, staff.getUsername());
            insertStaff.setString(3, staff.getPassword());
            insertStaff.setString(4, staff.getStatus().name().toLowerCase());
            insertStaff.setInt(5, person_id);
            int affectedRows = insertStaff.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert staff!");
            }
            return staff_id;
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
            if (appointment == null)
                appointment = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = appointment.format(formatter);
            insertPatient.setString(5, formattedDateTime);

            insertPatient.setString(6, patient.getEmergencyState().name());
            insertPatient.setString(7, patient.getPriority().name());
            insertPatient.setString(8, patient.getBloodType().name());

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

    /**
     * required fields: medicine, MedicineType
     * 
     * @param medicine pass the required fields mentioned above
     * @param amount   quantity of the medicine
     * @return message that shows the result of the operation.
     */
    public int addNewMedicine(Medicine medicine, int amount) {
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
            return medicine_id;
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
            System.out.println(sb.toString());
            return -1;
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
     * required fields: patient_id, staff_id, given_date, expire_date,
     * add at least one medicine to the receipt content
     * 
     * @param receipt pass the required fields mentioned above
     * @return message that shows the result of the operation.
     */
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
            if (medicines.size() == 0)
                throw new SQLException("Receipt must have at least one medicine!");
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

    /**
     * required fields: name, surname, age, gender for person
     * emergency_state, priority, blood_type for patient
     * doctor_id, receipt_id, appointment, complaints optional
     * 
     * @param patient pass the required fields mentioned above
     * @return message that shows the result of the operation.
     */
    public String addNewPatient(Patient patient) {
        try {
            conn.setAutoCommit(false);
            ArrayList<Boolean> results = new ArrayList<>();
            int patient_id = insertPerson(patient);
            if (patient_id == -1)
                results.add(false);
            else
                results.add(true);
            Boolean result = insertPatient(patient_id, patient.getDoctorId(), patient.getReceiptId(), patient);
            results.add(result);

            if (results.contains(false)) {
                throw new SQLException("Couldn't insert patient!");
            }
            conn.commit();
            return "Patient added successfully!";
            // if any error occurs, sql will rollback in the catch block
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Adding new patient failed: " + e.getMessage() + "\n");
            try {
                sb.append("Performing rollback \n");
                conn.rollback();
                sb.append("Adding new patient failed: " + e.getMessage() + "\n");
            } catch (SQLException e2) {
                sb.append("Oh boy! Things are really bad! " + e2.getMessage() + "\n");
                sb.append("Adding new patient failed: " + e2.getMessage() + "\n");
            }
            System.out.println(sb.toString());
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

    /**
     * required fields: name, surname, age, gender for person
     * username, password, status for staff
     * phone, email, address for contact of the staff.
     * WARNING: if you add a doctor, you must pass Doctor object
     * to retrieve the doctor's expertise.
     * if you add a nurse, you must pass Nurse object
     * to retrieve the nurse's expertise.
     * else, no workingArea or no expertise will be added.
     * 
     * @param staff pass the required fields mentioned above
     * @return message that shows the result of the operation.
     */
    public String addNewStaff(Staff staff) {
        try {
            conn.setAutoCommit(false);
            ArrayList<Boolean> results = new ArrayList<>();
            int person_id = insertPerson(staff);
            if (person_id == -1)
                results.add(false);
            else
                results.add(true);

            int staff_id = insertStaff(person_id, staff);

            Boolean result = insertContact(person_id, staff.getContact());
            results.add(result);

            if (staff.getStatus() == Status.DOCTOR) {
                Doctor doctor = new Doctor();
                if (staff instanceof Doctor)
                    doctor = (Doctor) staff;
                boolean doctorResult = insertDoctor(staff_id, doctor);
                results.add(doctorResult);
            }

            if (staff.getStatus() == Status.NURSE) {
                Nurse nurse = new Nurse();
                if (staff instanceof Nurse)
                    nurse = (Nurse) staff;
                boolean nurseResult = insertNurse(staff_id, nurse);
                results.add(nurseResult);
            }

            if (results.contains(false)) {
                throw new SQLException("Couldn't insert staff!");
            }
            conn.commit();
            return "Staff added successfully!";
            // if any error occurs, sql will rollback in the catch block
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Adding new staff failed: " + e.getMessage() + "\n");
            try {
                sb.append("Performing rollback \n");
                conn.rollback();
                sb.append("Adding new staff failed: " + e.getMessage() + "\n");
            } catch (SQLException e2) {
                sb.append("Oh boy! Things are really bad! " + e2.getMessage() + "\n");
                sb.append("Adding new staff failed: " + e2.getMessage() + "\n");
            }
            System.out.println(sb.toString());
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

    /*
     * required patient id,
     * updates appointment, complaints, doctor_id
     */
    public String updatePatient(Patient editedPatient) {
        try {
            conn.setAutoCommit(false);
            updatePatientByPersonId.setInt(1, editedPatient.getDoctorId());
            updatePatientByPersonId.setString(2, editedPatient.getComplaint());
            LocalDateTime appointment = editedPatient.getAppointment();
            if (appointment == null)
                appointment = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = appointment.format(formatter);
            updatePatientByPersonId.setString(3, formattedDateTime);
            updatePatientByPersonId.setInt(4, editedPatient.getId());

            int result = updatePatientByPersonId.executeUpdate();
            if (result != 1) {
                throw new SQLException("Couldn't update patient!");
            }
            conn.commit();
            return "Patient updated successfully!";
            // if any error occurs, sql will rollback in the catch block
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Updating patient failed: " + e.getMessage() + "\n");
            try {
                sb.append("Performing rollback \n");
                conn.rollback();
                sb.append("Updating patient failed: " + e.getMessage() + "\n");
            } catch (SQLException e2) {
                sb.append("Oh boy! Things are really bad! " + e2.getMessage() + "\n");
                sb.append("Updating patient failed: " + e2.getMessage() + "\n");
            }
            System.out.println(sb.toString());
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