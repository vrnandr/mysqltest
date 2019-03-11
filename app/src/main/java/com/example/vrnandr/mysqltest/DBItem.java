package com.example.vrnandr.mysqltest;

public class DBItem {
    String prod_id;
    String prod_name;
    Integer prod_kol;

    public DBItem(String prod_id, String prod_name, Integer prod_kol) {
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.prod_kol = prod_kol;
    }
}
