/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * File Name: DBConnection
 * Author: Gagandeep kaur Sangha, ID 041004212
 * Course: CST8288
 * Lab:2
 * Date:11/17/2024
 * Professor: Sazzad Hossain
*/
//package com.algonquin.cst8288.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/indywinners";
    private static final String USER = "root";
    private static final String PASSWORD = "Gagan@0510";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
    }
}
