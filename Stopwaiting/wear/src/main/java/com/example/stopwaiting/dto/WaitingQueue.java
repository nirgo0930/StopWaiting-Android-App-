package com.example.stopwaiting.dto;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaitingQueue implements Serializable {
    private Long qId;
    private String queueName;
    private String time;
    private int maxPerson;
    private ArrayList<UserInfo> waitingPersonList = new ArrayList<>();

    public WaitingQueue(Long qId, String queueName, String time, int maxPerson) {
        this.qId = qId;
        this.queueName = queueName;
        this.time = time;
        this.maxPerson = maxPerson;
    }

}
