package com.example.stopwaiting.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class WaitingQueue implements Serializable {
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

    public WaitingQueue(Long qId, String queueName, String time, int maxPerson, ArrayList<UserInfo> waitingPersonList) {
        this.qId = qId;
        this.queueName = queueName;
        this.time = time;
        this.maxPerson = maxPerson;
        this.waitingPersonList = waitingPersonList;
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
        if (waitingPersonList.size() < maxPerson) {
            if (!waitingPersonList.contains(n)) {
                waitingPersonList.add(n);
            } else {
                return 1;
            }
            return 0;
        } else {
            return 2;
        }

    }

    public boolean removeWPerson(Long n) {
        for (int i = 0; i < waitingPersonList.size(); i++) {
            if (waitingPersonList.get(i).getStudentCode().equals(n)) {
                waitingPersonList.remove(i);
                return true;
            }
        }
        return false;
    }
}