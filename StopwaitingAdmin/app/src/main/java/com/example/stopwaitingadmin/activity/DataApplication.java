package com.example.stopwaitingadmin.activity;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.example.stopwaitingadmin.dto.AdminWaitingListItem;
import java.util.ArrayList;

public class DataApplication extends Application {
    static public boolean isTest = false;

    static public ArrayList<AdminWaitingListItem> testAdminDBList = new ArrayList<>();
//    static public ArrayList<WaitingQueue> testWaitingQueueDBList;
    static public Long qCnt;

    static public RequestQueue requestQueue;

    static public String serverURL = "http://192.168.25.37:8080/api/v1";
    private String path = "/my_path";


    public ArrayList<AdminWaitingListItem> getTestAdminDBList() {
        return testAdminDBList;
    }

    public void setTestAdminDBList(ArrayList<AdminWaitingListItem> testAdminDBList) {
        this.testAdminDBList = testAdminDBList;
    }


//    public static ArrayList<WaitingQueue> getTestWaitingQueueDBList() {
//        return testWaitingQueueDBList;
//    }

//    public static void setTestWaitingQueueDBList(ArrayList<WaitingQueue> testWaitingQueueDBList) {
//        DataApplication.testWaitingQueueDBList = testWaitingQueueDBList;
//    }




}