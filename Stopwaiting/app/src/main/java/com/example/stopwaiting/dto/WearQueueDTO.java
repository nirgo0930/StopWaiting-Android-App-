package com.example.stopwaiting.dto;

import java.io.Serializable;

public class WearQueueDTO implements Serializable {
    private Long qId;
    private String queueName;
    private String time;
    private int myNum;
    private Double latitude; //위도
    private Double longitude; //경도


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

    public int getMyNum() {
        return myNum;
    }

    public void setMyNum(int myNum) {
        this.myNum = myNum;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public WearQueueDTO() {
    }

    public WearQueueDTO(UserInfo user, WaitingQueue data, WaitingInfo wData) {
        this.qId = data.getQId();
        this.queueName = data.getQueueName();
        this.time = data.getTime();
        this.myNum = data.getWaitingPersonList().indexOf(user);
        this.longitude = wData.getLongitude();
        this.latitude = wData.getLatitude();
    }

    public WearQueueDTO(Long qId, String queueName, String time, int myNum, Double latitude, Double longitude) {
        this.qId = qId;
        this.queueName = queueName;
        this.time = time;
        this.myNum = myNum;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}