package com.example.stopwaiting.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class WearDTO implements Serializable {
    private String name;
    private Long studentCode;
    private String tel;

    private Long qId;
    private String queueName;
    private String time;
    private int maxPerson;
    private ArrayList<UserInfo> waitingPersonList = new ArrayList<>();



}
