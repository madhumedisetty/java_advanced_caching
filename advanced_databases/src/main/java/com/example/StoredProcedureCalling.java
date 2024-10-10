package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class StoredProcedureCalling {
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/test_db_poc"; // Update with your database URL
    private static final String USER = "root"; // Update with your MySQL username
    private static final String PASSWORD = "root"; // Update with your MySQL password

    public static void main(String[] args) {
        Connection connection = null;
        CallableStatement callableStatement = null;

        try {
            // Establishing a connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established.");

            // Prepare the call for the stored procedure
            callableStatement = connection.prepareCall("{CALL INSERT_MILLION_RECORDS()}");

            // Execute the stored procedure
            callableStatement.execute();
            System.out.println("Stored procedure executed successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Closing resources
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}