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

import java.util.ArrayList;

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
/*        final MyListViewAdapter adapter = new MyListViewAdapter(this, null, this);

        MyViewModel viewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        viewModel.getAllData().observe(this, new Observer<ArrayList<DBItem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<DBItem> dbItems) {

            }
        });*/



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute();
            }
        });



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
        new MyUpdateTask().execute("UPDATE tbl_test SET prod_kol = prod_kol "+whatToDo+" 1 WHERE prod_id = "+id);
    }


    public void viewProgressDialog(String string){
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage(string);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismissProgressDialog(){
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    class MyTask extends AsyncTask<String,Void, Integer>{

        private ArrayList<DBItem> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
            viewProgressDialog("Выполнение операции. Подождите...");

        }


        @Override
        protected Integer doInBackground(String... strings) {

            DB db = DB.getInstance();
            list = db.SelectAll();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dismissProgressDialog();
            lv.setAdapter(new MyListViewAdapter(App.getContext(),list,MainActivity.this));
        }
    }


    class MyUpdateTask extends AsyncTask<String,Void, Integer>{
        private ArrayList<DBItem> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
            viewProgressDialog("Выполнение операции. Подождите...");

        }


        @Override
        protected Integer doInBackground(String... strings) {
            DB db = DB.getInstance();
            if (db.UpdateData(strings[0])>0)
                list = db.SelectAll();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dismissProgressDialog();
            if (list.size()>0)
                lv.setAdapter(new MyListViewAdapter(App.getContext(),list,MainActivity.this));
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
