package com.example.stopwaitingadmin.dto;

public class AdminWaitingListItem {

    public AdminWaitingListItem(int imgId, String txtName, String txtUser, String txtLocation) {
        mImgId = imgId;
        mTxtName = txtName;
        mTxtUser = txtUser;
        mTxtLocation = txtLocation;
    }

    private int mImgId;
    private String mTxtName;
    private String mTxtUser;
    private String mTxtLocation;

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
