package com.example.stopwaiting.activity;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WearQueueDTO;
import com.example.stopwaiting.service.MessageService;

import java.util.ArrayList;

public class DataApplication extends Application {
    public static UserInfo currentUserInfo = new UserInfo();
    public static ArrayList<WearQueueDTO> myWaiting = new ArrayList<>();


    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor autoEdit;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = MessageService.sharedPreferences;
        autoEdit = MessageService.sharedPreferences.edit();

    }
}
