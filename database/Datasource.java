package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Contact;
import model.Doctor;
import model.Manager;
import model.Nurse;
import model.Patient;
import model.Pharmacist;
import model.Receptionist;
import model.Staff;
import model.enums.BloodType;
import model.enums.EmergencyState;
import model.enums.Gender;
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
    private static final String COLUMN_MEDICINE_STOCK = "stock";

    private static final String TABLE_MEDICINE_RECEIPT = "medicine_receipt";
    private static final String COLUMN_MEDICINE_RECEIPT_RECEIPT_ID = "id";
    private static final String COLUMN_MEDICINE_RECEIPT_MEDICINE_ID = "medicine_id";
    private static final String COLUMN_MEDICINE_RECEIPT_AMOUNT = "amount";

    private static final String QUERY_LOGIN = "SELECT " + COLUMN_STAFF_STATUS + " FROM " + TABLE_STAFF
            + " WHERE " + COLUMN_STAFF_USERNAME + " = ? AND " + COLUMN_STAFF_PASSWORD + " = ?";

    private static final String QUERY_STAFF_BY_USERNAME = "SELECT * FROM " + TABLE_STAFF +
            " INNER JOIN " + TABLE_PERSON + " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_PERSON
            + "." + COLUMN_PERSON_ID +
            " INNER JOIN " + TABLE_CONTACT + " ON " + TABLE_STAFF + "." + COLUMN_STAFF_PERSON_ID + " = " + TABLE_CONTACT
            + "." + COLUMN_CONTACT_PERSON_ID +
            " WHERE " + COLUMN_STAFF_USERNAME + " = ?";

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

    private static final String UPDATE_PERSON_BY_PERSON_ID = "UPDATE " + TABLE_PERSON + " SET " +
            COLUMN_PERSON_NAME + " = ?, " +
            COLUMN_PERSON_SURNAME + " = ?, " +
            COLUMN_PERSON_GENDER + " = ?, " +
            COLUMN_PERSON_AGE + " = ? " +
            "WHERE " + COLUMN_PERSON_ID + " = ?";

    private static final String UPDATE_CONTACT_BY_PERSON_ID = "UPDATE " + TABLE_CONTACT + " SET " +
            COLUMN_CONTACT_PHONE + " = ?, " +
            COLUMN_CONTACT_EMAIL + " = ?, " +
            COLUMN_CONTACT_ADDRESS + " = ? " +
            "WHERE " + COLUMN_CONTACT_PERSON_ID + " = ?";

    private Connection conn;

    private PreparedStatement queryLogin;
    private PreparedStatement queryStaffByUsername;
    private PreparedStatement queryStaffIdByUsername;
    private PreparedStatement queryPatientsByStaffId;
    private PreparedStatement queryStaffById;
    private PreparedStatement queryDoctors;
    private PreparedStatement queryDoctorExpretiseByStaffId;
    private PreparedStatement queryNurseWorkingAreaByStaffId;
    private PreparedStatement updatePersonByPersonId;
    private PreparedStatement updateContactByPersonId;

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
            queryStaffByUsername = conn.prepareStatement(QUERY_STAFF_BY_USERNAME);
            queryStaffIdByUsername = conn.prepareStatement(QUERY_STAFF_ID_BY_USERNAME);
            queryPatientsByStaffId = conn.prepareStatement(QUERY_PATIENTS_BY_STAFF_ID);
            queryStaffById = conn.prepareStatement(QUERY_STAFF_BY_ID);
            queryDoctorExpretiseByStaffId = conn.prepareStatement(QUERY_DOCTOR_EXPRETISE_BY_STAFF_ID);
            queryNurseWorkingAreaByStaffId = conn.prepareStatement(QUERY_NURSE_WORKING_AREA_BY_STAFF_ID);
            updatePersonByPersonId = conn.prepareStatement(UPDATE_PERSON_BY_PERSON_ID);
            updateContactByPersonId = conn.prepareStatement(UPDATE_CONTACT_BY_PERSON_ID);

            // insertIntoArtists = conn.prepareStatement(INSERT_ARTIST,
            // Statement.RETURN_GENERATED_KEYS);

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

            if (queryStaffByUsername != null) {
                queryStaffByUsername.close();
            }

            if (queryPatientsByStaffId != null) {
                queryPatientsByStaffId.close();
            }

            if (queryStaffIdByUsername != null) {
                queryStaffIdByUsername.close();
            }

            if (queryStaffById != null) {
                queryStaffById.close();
            }

            if (queryDoctors != null) {
                queryDoctors.close();
            }

            if (queryDoctorExpretiseByStaffId != null) {
                queryDoctorExpretiseByStaffId.close();
            }

            if (queryNurseWorkingAreaByStaffId != null) {
                queryNurseWorkingAreaByStaffId.close();
            }

            if (updatePersonByPersonId != null) {
                updatePersonByPersonId.close();
            }

            if (updateContactByPersonId != null) {
                updateContactByPersonId.close();
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
                doctor.setExpretise(results.getString(COLUMN_DOCTOR_EXPRETISE));
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
     * @param staff_id the id of the staff 
     * @return patient arraylist matching the staff id
     */
    public ArrayList<Patient> queyPatients(int staff_id){
        try {
            queryPatientsByStaffId.setInt(1, staff_id);
            ResultSet results = queryPatientsByStaffId.executeQuery();
            ArrayList<Patient> patients = new ArrayList<>();
            while(results.next()){
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
                if(complaint != null)
                    patient.setComplaint(complaint);
                else
                    patient.setComplaint(null);
                
                String dateString = results.getString(COLUMN_PATIENT_APPOINTMENT);
                if(dateString != null){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
                    patient.setAppointment(dateTime);
                }else{
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
     * @param staff a new staff object to update existing staff by id.
     * @return message that shows the result of the operation.
     */
    public String updateStaff(Staff staff) {

        try {
            conn.setAutoCommit(false);

            int personId = getPersonIdByStaffId(staff.getId());
            if (personId == -1) {
                return "Couldn't find the staff by id: " + staff.getId();
            } else {
                updatePersonByPersonId.setString(1, staff.getName());
                updatePersonByPersonId.setString(2, staff.getSurname());
                updatePersonByPersonId.setInt(3, staff.getAge());
                updatePersonByPersonId.setString(4, staff.getGender().toString());
                updatePersonByPersonId.setInt(5, personId);
                updateContactByPersonId.setString(1, staff.getContact().getPhone());
                updateContactByPersonId.setString(2, staff.getContact().getEmail());
                updateContactByPersonId.setString(3, staff.getContact().getAddress());
                updateContactByPersonId.setInt(4, personId);
                int affectedRows1 = updatePersonByPersonId.executeUpdate();
                int affectedRows2 = updateContactByPersonId.executeUpdate();
                if (affectedRows1 == 1 && affectedRows2 == 1) {
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

}