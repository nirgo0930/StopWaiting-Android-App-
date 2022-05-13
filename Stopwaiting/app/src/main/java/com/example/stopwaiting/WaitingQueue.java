package com.example.stopwaiting;

import java.util.ArrayList;

public class WaitingQueue {
    private String queueName;
    private String time;
    private int maxPerson;
    private ArrayList<String> waitingPersonList = new ArrayList<>();

    public WaitingQueue() {
    }

    public WaitingQueue(String queueName, String time, int maxPerson) {
        this.queueName = queueName;
        this.time = time;
        this.maxPerson = maxPerson;
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

    public ArrayList<String> getWaitingPersonList() {
        return waitingPersonList;
    }

    public void setWaitingPersonList(ArrayList<String> waitingPersonList) {
        this.waitingPersonList = waitingPersonList;
    }

    public int addWPerson(String n) {
        if (!(DataApplication.myWaiting.contains(this))) {
            if (waitingPersonList.size() < maxPerson) {
                DataApplication.myWaiting.add(this);
                waitingPersonList.add(n);
                return 0;
            } else {
                return 2;
            }
        } else {
            return 1;
        }
    }

}