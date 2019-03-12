package com.example.vrnandr.mysqltest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SelectAllTask extends AsyncTask<Void, Void, LiveData<ArrayList<DBItem>>> {

    private static final String url = "jdbc:mysql://asbest.dyndns.biz:3306/db_test";
    private static final String user = "user_varaa";
    private static final String password = "user_varaa";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private OnTaskCompleteListener taskCompleteListener;

    public SelectAllTask(OnTaskCompleteListener taskCompleteListener) {
        this.taskCompleteListener = taskCompleteListener;
    }

    @Override
    protected LiveData<ArrayList<DBItem>> doInBackground(Void... voids) {
        MutableLiveData<ArrayList<DBItem>> returnLiveData = new MutableLiveData<>();
        try{
            connection = DriverManager.getConnection(url,user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM tbl_test");
            ArrayList<DBItem> list = new ArrayList<>();
            while (resultSet.next()){
                list.add(new DBItem(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3)));
            }
            returnLiveData.postValue(list);

        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (connection!=null) connection.close();
                if (statement!=null) statement.close();
                if (resultSet!=null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return returnLiveData;
    }


    @Override
    protected void onPostExecute(LiveData<ArrayList<DBItem>> arrayListLiveData) {
        taskCompleteListener.onTaskComplete(arrayListLiveData);
    }
}
