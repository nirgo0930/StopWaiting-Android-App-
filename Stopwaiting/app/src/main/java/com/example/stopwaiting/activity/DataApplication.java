package com.example.stopwaiting.activity;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;

import java.util.ArrayList;

public class DataApplication extends Application {
    static public ArrayList<WaitingInfo> testDBList = new ArrayList<>();
    static public ArrayList<ImgItem> testImageDBList = new ArrayList<>();
    static public ArrayList<WaitingQueue> testWaitingQueueDBList = new ArrayList<>();
    static public ArrayList<WaitingQueue> myWaiting = new ArrayList<>();
    static Long qCnt;

    static public String serverURL = "http://dodam123.dothome.co.kr/Login.php";
    static public RequestQueue requestQueue;
    static public UserInfo currentUser = new UserInfo();

    public ArrayList<WaitingInfo> getTestDBList() {
        return testDBList;
    }

    public void setTestDBList(ArrayList<WaitingInfo> testDBList) {
        this.testDBList = testDBList;
    }

    public ArrayList<ImgItem> getTestImageDBList() {
        return testImageDBList;
    }

    public void setTestImageDBList(ArrayList<ImgItem> testImageDBList) {
        this.testImageDBList = testImageDBList;
    }

    public static ArrayList<WaitingQueue> getTestWaitingQueueDBList() {
        return testWaitingQueueDBList;
    }

    public static void setTestWaitingQueueDBList(ArrayList<WaitingQueue> testWaitingQueueDBList) {
        DataApplication.testWaitingQueueDBList = testWaitingQueueDBList;
    }

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
