package com.example.vrnandr.mysqltest;


import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLUtils {

    private Connection connection;

    private static final String url = "jdbc:mysql://asbest.dyndns.biz:3306/db_test";
    private static final String user = "user_varaa";
    private static final String password = "user_varaa";


    public MySQLUtils() {
        try {
            connection = DriverManager.getConnection(url,user, password);
        } catch ( SQLException e){
            e.printStackTrace();
        } finally {
            try{
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }


    }
}
