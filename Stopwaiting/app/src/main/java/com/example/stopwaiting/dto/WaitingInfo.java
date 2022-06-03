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

//    public WaitingInfo() {
//    }

    public WaitingInfo(Long adm, Long waitingId, Double latitude, Double longitude, String name, String locDetail, String info, String type, int maxPerson, ArrayList<String> urlList) {
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
//
//    public Long getAdminId() {
//        return adminId;
//    }
//
//    public void setAdminId(Long adminId) {
//        this.adminId = adminId;
//    }
//
//    public Long getWaitingId() {
//        return waitingId;
//    }
//
//    public void setWaitingId(Long waitingId) {
//        this.waitingId = waitingId;
//    }
//
//    public Double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(Double latitude) {
//        this.latitude = latitude;
//    }
//
//    public Double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(Double longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getLocDetail() {
//        return locDetail;
//    }
//
//    public void setLocDetail(String locDetail) {
//        this.locDetail = locDetail;
//    }
//
//    public String getInfo() {
//        return info;
//    }
//
//    public void setInfo(String info) {
//        this.info = info;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public int getMaxPerson() {
//        return maxPerson;
//    }
//
//    public void setMaxPerson(int maxPerson) {
//        this.maxPerson = maxPerson;
//    }
//
//    public ArrayList getTimetable() {
//        return timetable;
//    }
//
//    public void setTimetable(ArrayList timetable) {
//        this.timetable = timetable;
//    }
//
//    public ArrayList<String> getUrlList() {
//        return urlList;
//    }
//
//    public void setUrlList(ArrayList<String> urlList) {
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
