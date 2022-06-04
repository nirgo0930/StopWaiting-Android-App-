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
    private Long id; //id
    private Double latitude; //위도
    private Double longitude; //경도
    private String name; //웨이팅 명칭
    private String locDetail; //상세 위치 DB134
    private String info; //가게 설명
    private String type; //normal time
    private int maxPerson; //최대인원
    private ArrayList<String> timetable = new ArrayList(); //운영 타임 ex)12:00 13:00

    public WaitingInfo(Long adm, Long id, Double latitude, Double longitude, String name, String locDetail, String info, String type, int maxPerson) {
        this.adminId = adm;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.locDetail = locDetail;
        this.info = info;
        this.type = type;
        this.maxPerson = maxPerson;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaitingInfo info = (WaitingInfo) o;

        if (!Objects.equals(this.id, info.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
