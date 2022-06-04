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

    public UserInfo(String name, Long studentCode, String tel) {
        this.name = name;
        this.studentCode = studentCode;
        this.tel = tel;
    }
}
