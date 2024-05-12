package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectioHelper {
    Connection con;
    String uname, pass, ip, port, database;

    public Connection connection() {
        ip = "127.0.0.1"; // Assuming your MySQL server is running locally
        database = "lab2";
        port = "3306";
        uname = "root";
        pass = "";

        Connection connect = null;
        String ConnectionURL = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ConnectionURL = "jdbc:mysql://" + ip + ":" + port + "/" + database;
            connect = DriverManager.getConnection(ConnectionURL, uname, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            Log.e("ERROR sql",ex.getMessage());
        }
        return connect;
    }

}
