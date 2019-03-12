package com.example.vrnandr.mysqltest;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;

public interface OnTaskCompleteListener {
    void onTaskComplete(LiveData<ArrayList<DBItem>> data);
}
