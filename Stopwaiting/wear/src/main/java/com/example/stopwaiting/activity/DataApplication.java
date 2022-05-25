package com.example.stopwaiting.activity;

import android.app.Application;

import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WearQueueDTO;

import java.util.ArrayList;

public class DataApplication extends Application {
    public static UserInfo currentUserInfo = new UserInfo();
    public static ArrayList<WearQueueDTO> myWaiting = new ArrayList<>();
}
