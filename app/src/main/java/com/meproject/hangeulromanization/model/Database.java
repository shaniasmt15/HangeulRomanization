package com.meproject.hangeulromanization.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://192.168.100.12:3306/hangeul_db";
        String user = "admin";
        String pass = "admin123";
        Connection connection = DriverManager.getConnection(url,user,pass);
        return connection;
    }
}
