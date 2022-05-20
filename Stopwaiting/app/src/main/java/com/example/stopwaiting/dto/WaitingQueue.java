package com.example.stopwaiting.dto;

import com.example.stopwaiting.activity.DataApplication;

import java.util.ArrayList;

public class WaitingQueue {
    private Long qId;
    private String queueName;
    private String time;
    private int maxPerson;
    private ArrayList<UserInfo> waitingPersonList = new ArrayList<>();

    public WaitingQueue() {
    }

    public WaitingQueue(Long qId, String queueName, String time, int maxPerson) {
        this.qId = qId;
        this.queueName = queueName;
        this.time = time;
        this.maxPerson = maxPerson;
    }


    public Long getQId() {
        return qId;
    }

    public void setQId(Long qId) {
        this.qId = qId;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(int maxPerson) {
        this.maxPerson = maxPerson;
    }

    public ArrayList<UserInfo> getWaitingPersonList() {
        return waitingPersonList;
    }

    public void setWaitingPersonList(ArrayList<UserInfo> waitingPersonList) {
        this.waitingPersonList = waitingPersonList;
    }

    public int addWPerson(UserInfo n) {
        if (!(DataApplication.myWaiting.contains(this))) {
            if (waitingPersonList.size() < maxPerson) {
                if (n.getName().equals(DataApplication.currentUser.getName())) {
                    DataApplication.myWaiting.add(this); //test code
                }
                waitingPersonList.add(n);
                return 0;
            } else {
                return 2;
            }
        } else {
            return 1;
        }
    }

    public int removeWPerson(String n) {
        if (waitingPersonList.contains(n)) {
            waitingPersonList.remove(waitingPersonList.indexOf(n));
            if (n.equals(DataApplication.currentUser)) {
                DataApplication.myWaiting.remove(this); //test code
            }
            return 0;
        } else {
            return 1;
        }
    }

}
