package com.example.vrnandr.mysqltest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static  ProgressDialog pDialog;
    private static TextView tv;
    private static ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv = findViewById(R.id.tv);
        lv = findViewById(R.id.list_view);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute("SELECT * FROM tbl_test");
            }
        });

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    static class MyTask extends AsyncTask<String,Void, Integer>{

        private static final String url = "jdbc:mysql://asbest.dyndns.biz:3306/db_test";
        private static final String user = "user_varaa";
        private static final String password = "user_varaa";
        private Connection connection;
        private Statement statement;
        private ResultSet resultSet;

        private ArrayList<DBItem> list;
        private Integer cnt=-1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
            pDialog = new ProgressDialog(App.getContext());
            pDialog.setMessage("Загрузка продуктов. Подождите...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Integer doInBackground(String... strings) {

            try {
                connection = DriverManager.getConnection(url,user, password);
                statement = connection.createStatement();
                resultSet = statement.executeQuery(strings[0]);

                while (resultSet.next()){
                    list.add(new DBItem(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3)));
                }





            } catch ( SQLException e){
                e.printStackTrace();
            } finally {
                try{
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
                try{
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
                try{
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            pDialog.dismiss();
            lv.setAdapter(new MyListViewAdapter(App.getContext(),list));
            //tv.setText(cnt.toString());
        }
    }


}


/*

CREATE TABLE `tbl_test` (
  `prod_id` varchar(8) NOT NULL DEFAULT '00000000',
  `prod_name` varchar(255) DEFAULT NULL,
  `prod_kol` int(2) DEFAULT NULL,
  PRIMARY KEY (`prod_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
 */
