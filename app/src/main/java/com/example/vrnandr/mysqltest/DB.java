package com.example.vrnandr.mysqltest;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {
    private static DB instance;

    private static final String url = "jdbc:mysql://asbest.dyndns.biz:3306/db_test";
    private static final String user = "user_varaa";
    private static final String password = "user_varaa";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private DB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        open();
   }

    public static DB getInstance(){
        if (instance==null) instance=new DB();

        return instance;
    }

    public boolean open(){
        boolean result = true;
        try{
            connection = DriverManager.getConnection(url,user, password);
            statement = connection.createStatement();
        } catch (SQLException e){
            e.printStackTrace();
            result =false;
        }
        return result;
    }

    public void close(){
        try{
            if (connection!=null) connection.close();
            if (statement!=null) statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public ArrayList<DBItem> SelectAll(){
        ArrayList<DBItem> list = new ArrayList<>();
        try{
            resultSet = statement.executeQuery("SELECT * FROM tbl_test");
            list = new ArrayList<>();
            while (resultSet.next()){
                list.add(new DBItem(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3)));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
            } catch (SQLException e){
                e.printStackTrace();
            }

        }

        return list;
    }

    public Integer UpdateData(String sql){
        try{
            return statement.executeUpdate(sql);
        } catch (SQLException e){
          e.printStackTrace();
          return null;
        }

    }

}
