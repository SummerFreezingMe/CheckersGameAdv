package ru.vsu.cs.bykov.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    public static Connection connection;
    public static final String USER_NAME = "root";
    public static final String PASSWORD="root";
    public static final String URL="jdbc:mysql://localhost:3307/mysql";


    public static Statement statement;
    static {
        try {
            connection= DriverManager.getConnection(URL,USER_NAME,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    static {
        try {
           statement= connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
