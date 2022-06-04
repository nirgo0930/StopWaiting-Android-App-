package com.example.stopwaiting.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaitingInfo implements Serializable {
    private Long adminId; //개설자
    private Long waitingId; //id
    private Double latitude; //위도
    private Double longitude; //경도
    private String name; //웨이팅 명칭
    private String locDetail; //상세 위치 DB134
    private String info; //가게 설명
    private String type; //normal time
    private int maxPerson; //최대인원
    private ArrayList<String> timetable = new ArrayList(); //운영 타임 ex)12:00 13:00
    private ArrayList<String> urlList = new ArrayList<>();
    private ArrayList<Long> queueList = new ArrayList<>();

    public WaitingInfo(Long adm, Long waitingId, Double latitude, Double longitude, String name, String locDetail, String info, String type, int maxPerson, ArrayList<String> urlList, ArrayList<Long> queueList) {
        this.adminId = adm;
        this.waitingId = waitingId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.locDetail = locDetail;
        this.info = info;
        this.type = type;
        this.maxPerson = maxPerson;
        this.urlList = urlList;
        this.queueList = queueList;
    }

//    public WaitingInfo(Long adm, Long waitingId, Double latitude, Double longitude, String name, String locDetail, String info, String type, int maxPerson, ArrayList timetable, ArrayList<String> urlList) {
//        this.adminId = adm;
//        this.waitingId = waitingId;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.name = name;
//        this.locDetail = locDetail;
//        this.info = info;
//        this.type = type;
//        this.maxPerson = maxPerson;
//        this.timetable = timetable;
//        this.urlList = urlList;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaitingInfo info = (WaitingInfo) o;

        if (!Objects.equals(this.waitingId, info.waitingId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return waitingId != null ? waitingId.hashCode() : 0;
    }

}
