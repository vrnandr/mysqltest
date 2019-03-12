package com.example.vrnandr.mysqltest;

import android.app.ProgressDialog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnListViewButtonClickListener{

    private static ProgressDialog pDialog;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = findViewById(R.id.list_view);
        final MyListViewAdapter adapter = new MyListViewAdapter(this, null, this);

        MyViewModel viewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        viewModel.getAllData().observe(this, new Observer<ArrayList<DBItem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<DBItem> dbItems) {
                adapter.updateData(dbItems);
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    @Override
    public void onClick(String id, String whatToDo) {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Выполнение операции. Подождите...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        new MyUpdateTask().execute("UPDATE tbl_test SET prod_kol = prod_kol "+whatToDo+" 1 WHERE prod_id = "+id);


    }


    public void viewProgressDialog(String string){
        pDialog = new ProgressDialog(MainActivity.this);
        //pDialog.setMessage("Выполнение операции. Подождите...");
        pDialog.setMessage(string);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismissProgressDialog(){
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    class MyTask extends AsyncTask<String,Void, Integer>{

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
            lv.setAdapter(new MyListViewAdapter(App.getContext(),list,MainActivity.this));
            //tv.setText(cnt.toString());
        }
    }


    class MyUpdateTask extends AsyncTask<String,Void, Integer>{

        private static final String url = "jdbc:mysql://asbest.dyndns.biz:3306/db_test";
        private static final String user = "user_varaa";
        private static final String password = "user_varaa";
        private Connection connection;
        private Statement statement;
        private ResultSet resultSet;

        private ArrayList<DBItem> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();

        }


        @Override
        protected Integer doInBackground(String... strings) {

            try {
                connection = DriverManager.getConnection(url,user, password);
                statement = connection.createStatement();
                statement.executeUpdate(strings[0]);

                resultSet = statement.executeQuery("SELECT * FROM tbl_test");

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
            lv.setAdapter(new MyListViewAdapter(App.getContext(),list,MainActivity.this));
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
