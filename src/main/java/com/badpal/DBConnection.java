package com.badpal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author garyl
 */
public class DBConnection {

    private static Connection connection;
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/s60737_bp";
    private static final String DATABASE_USER = "s60737";
    private static final String DATABASE_PASSWORD = "mKdUu3b1rya7";
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        try {
            Class.forName(DATABASE_DRIVER);
            connection = DriverManager.getConnection(
                    DATABASE_URL,
                    DATABASE_USER,
                    DATABASE_PASSWORD
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.getMessage();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
