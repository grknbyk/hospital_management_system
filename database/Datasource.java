package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Datasource {

    private static final String DB_NAME = "hospital.db";

    private static final String CONNECTION_STRING = "jdbc:sqlite:database\\"+ DB_NAME;

    private static final String TABLE_STAFF = "staffs";
    private static final String COLUMN_STAFF_ID = "id";
    private static final String COLUMN_STAFF_USERNAME = "username";
    private static final String COLUMN_STAFF_PASSWORD = "password";
    private static final String COLUMN_STAFF_STATUS = "status";
    private static final int INDEX_STAFF_ID = 1;
    private static final int INDEX_STAFF_USERNAME = 2;
    private static final int INDEX_STAFF_PASSWORD = 3;
    private static final int INDEX_STAFF_STATUS = 4;

    private static final int ORDER_BY_NONE = 1;
    private static final int ORDER_BY_ASC = 2;
    private static final int ORDER_BY_DESC = 3;

    private static final String QUERY_LOGIN = "SELECT " + COLUMN_STAFF_STATUS + " FROM " + TABLE_STAFF 
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