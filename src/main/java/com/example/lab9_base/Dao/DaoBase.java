package com.example.lab9_base.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DaoBase {
    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:mysql://localhost:3306/lab9?serverTimezone=America/Lima";
        String user = "root";
        String password = "iweb_proyecto";
        return DriverManager.getConnection(url, user, password);
    }
}