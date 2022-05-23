package com.example.stopwaiting.activity;

import android.app.Application;

import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;

import java.util.ArrayList;

public class DataApplication extends Application {
    public static UserInfo currentUserInfo = new UserInfo();
    public static ArrayList<WaitingQueue> myWaiting = new ArrayList<>();
    public static ArrayList<WaitingInfo> waitingInfos = new ArrayList<>();
}
