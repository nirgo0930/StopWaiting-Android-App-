package com.example.stopwaiting.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    private String name;
    private Long studentCode;
    private String tel;
    private String token = "temp";

//    public UserInfo() {
//    }

    public UserInfo(String name, Long studentCode, String tel) {
        this.name = name;
        this.studentCode = studentCode;
        this.tel = tel;
    }

//    public UserInfo(String name, Long studentCode, String tel, String token) {
//        this.name = name;
//        this.studentCode = studentCode;
//        this.tel = tel;
//        this.token = token;
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
//    public Long getStudentCode() {
//        return studentCode;
//    }
//
//    public void setStudentCode(Long studentCode) {
//        this.studentCode = studentCode;
//    }
//
//    public String getTel() {
//        return tel;
//    }
//
//    public void setTel(String tel) {
//        this.tel = tel;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
}
