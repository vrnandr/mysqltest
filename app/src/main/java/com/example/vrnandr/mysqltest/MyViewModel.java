package com.example.vrnandr.mysqltest;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class MyViewModel extends AndroidViewModel implements OnTaskCompleteListener {

    private LiveData<ArrayList<DBItem>> allItemsListLiveData;

    public MyViewModel(@NonNull Application application) {
        super(application);
        new SelectAllTask(this).execute();
    }

    @Override
    public void onTaskComplete(LiveData<ArrayList<DBItem>> data) {
        allItemsListLiveData = data;
    }

    public LiveData<ArrayList<DBItem>> getAllData(){
        return allItemsListLiveData;
    }
}
