package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Datasource {

    public static final String DB_NAME = "hospital.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Lenovo\\Desktop\\hospital management system\\data\\"+ DB_NAME;

    public static final String TABLE_STAFF = "staffs";
    public static final String COLUMN_STAFF_ID = "id";
    public static final String COLUMN_STAFF_USERNAME = "username";
    public static final String COLUMN_STAFF_PASSWORD = "password";
    public static final String COLUMN_STAFF_STATUS = "status";
    public static final int INDEX_STAFF_ID = 1;
    public static final int INDEX_STAFF_USERNAME = 2;
    public static final int INDEX_STAFF_PASSWORD = 3;
    public static final int INDEX_STAFF_STATUS = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_LOGIN = "SELECT " + COLUMN_STAFF_STATUS + " FROM " + TABLE_STAFF 
    + " WHERE " + COLUMN_STAFF_USERNAME + " = ? AND " + COLUMN_STAFF_PASSWORD + " = ?";



    private Connection conn;

    private PreparedStatement queryLogin;

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

}