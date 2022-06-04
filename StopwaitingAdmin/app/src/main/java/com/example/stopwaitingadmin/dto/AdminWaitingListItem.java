package com.example.stopwaitingadmin.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class AdminWaitingListItem {
    private Long wId;               //웨이팅 id
    private int mImgId;             //이미지
    private String mTxtName;        //웨이팅이름
    private String mTxtUser;        //개설자이름
    private String mTxtLocation;    //장소이름

    public AdminWaitingListItem(Long wId, int imgId, String txtName, String txtUser, String txtLocation) {
        this.wId = wId;
        mImgId = imgId;
        mTxtName = txtName;
        mTxtUser = txtUser;
        mTxtLocation = txtLocation;
    }

    public Long getId() {
        return wId;
    }

    public void setId(Long wId) {
        this.wId = wId;
    }

    public int getImgId() {
        return mImgId;
    }

    public void setImgId(int mImgId) {
        this.mImgId = mImgId;
    }

    public String getTxtName() {
        return mTxtName;
    }

    public void setTxtName(String mTxtName) {
        this.mTxtName = mTxtName;
    }

    public String getTxtUser() {
        return mTxtUser;
    }

    public void setTxtUser(String mTxtUser) {
        this.mTxtUser = mTxtUser;
    }

    public String getTxtLocation() {
        return mTxtLocation;
    }

    public void setTxtLocation(String mTxtLocation) {
        this.mTxtLocation = mTxtLocation;
    }
}
