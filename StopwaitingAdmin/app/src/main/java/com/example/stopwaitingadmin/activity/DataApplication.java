package com.example.stopwaitingadmin.activity;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.example.stopwaitingadmin.dto.AdminWaitingListItem;
import com.example.stopwaitingadmin.dto.ReportedUserListItem;

import java.util.ArrayList;

public class DataApplication extends Application {
    static public boolean isTest = false;

    static public ArrayList<AdminWaitingListItem> testAdminDBList = new ArrayList<>();
    static public ArrayList<ReportedUserListItem> testReportedUser = new ArrayList<>();
    static public Long qCnt;

    static public ArrayList<AdminWaitingListItem> adminWaitingQueue = new ArrayList<>();
    static public ArrayList<ReportedUserListItem> reportedUserQueue = new ArrayList<>();


    static public RequestQueue requestQueue;

//    static public String serverURL = "http://stopwaitingserver-env-1.eba-pxdbitvs.ap-northeast-2.elasticbeanstalk.com/api/v1";
//    static public String imgURL = "http://stopwaitingserver-env-1.eba-pxdbitvs.ap-northeast-2.elasticbeanstalk.com";
        static public String serverURL = "http://192.168.231.221:8080/api/v1";
        static public String imgURL = "http://192.168.231.221:8080";
    private String path = "/my_path";


    public ArrayList<AdminWaitingListItem> getTestAdminDBList() {
        return testAdminDBList;
    }

    public void setTestAdminDBList(ArrayList<AdminWaitingListItem> testAdminDBList) {
        this.testAdminDBList = testAdminDBList;
    }



}