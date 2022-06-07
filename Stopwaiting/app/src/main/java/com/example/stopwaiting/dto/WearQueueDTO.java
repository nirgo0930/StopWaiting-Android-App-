package com.example.stopwaiting.dto;

import android.util.Log;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WearQueueDTO implements Serializable {
    private Long qId;
    private Long wId;
    private String queueName;
    private String time;
    private int myNum;
    private Double latitude; //위도
    private Double longitude; //경도

    public WearQueueDTO(UserInfo user, WaitingQueue data, WaitingInfo wData) {
        this.qId = data.getQId();
        this.wId = wData.getWaitingId();
        this.queueName = data.getQueueName();
        this.time = data.getTime();
        for (UserInfo userInfo : data.getWaitingPersonList()) {
            if (userInfo.getStudentCode().equals(user)) {
                this.myNum = data.getWaitingPersonList().indexOf(user);
                break;
            }
        }
        this.longitude = wData.getLongitude();
        this.latitude = wData.getLatitude();

        Log.e("newDTO", String.valueOf(wId) + "/" + queueName);
    }
}