package com.example.stopwaitingadmin.activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.stopwaitingadmin.dto.ReportedUserListItem;
import com.example.stopwaitingadmin.dto.AdminWaitingListItem;
import com.google.android.gms.tasks.Task;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;

public class DataApplication extends Application {
    static public boolean isTest = true;

    static public ArrayList<AdminWaitingListItem> testAdminDBList = new ArrayList<>();
//    static public ArrayList<WaitingQueue> testWaitingQueueDBList;
    static public Long qCnt;

    static public RequestQueue requestQueue;

    static public String serverURL = "http://192.168.238.68:8080/api/v1";
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


    public boolean firstIsLater(String a, String b) {
        int hourA = Integer.valueOf(a.substring(0, 2));
        int minA = Integer.valueOf(a.substring(3));
        int hourB = Integer.valueOf(b.substring(0, 2));
        int minB = Integer.valueOf(b.substring(3));
        if (hourA > hourB) {
            return true;
        } else if (hourA == hourB) {
            if (minA > minB) {
                return true;
            }
        }
        return false;
    }

}