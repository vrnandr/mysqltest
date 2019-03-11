package com.example.vrnandr.mysqltest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyListViewAdapter extends BaseAdapter {

    private ResultSet resultSet;
    private ArrayList<DBItem> items;
    private LayoutInflater layoutInflater;

    public MyListViewAdapter(Context context, ArrayList<DBItem> list) {
        items = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DBItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view==null){
            view = layoutInflater.inflate(R.layout.list_view_item,parent,false);
        }

        TextView name = view.findViewById(R.id.name);
        TextView count = view.findViewById(R.id.count);

        DBItem item = getItem(position);

        name.setText(item.prod_name);
        count.setText(item.prod_kol);

        return view;
    }
}
