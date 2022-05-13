package com.example.stopwaiting;

import android.net.Uri;

public class MyWaitingListItem {
    private Uri imgUri;
    private String name;
    private int waitingCnt;
    private String locDetail;

    public MyWaitingListItem(Uri imgId, String txtName, int txtWaitingCnt, String txtLocDetail) {
        this.imgUri = imgId;
        this.name = txtName;
        this.waitingCnt = txtWaitingCnt;
        this.locDetail = txtLocDetail;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri mImgUri) {
        this.imgUri = mImgUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String mTxtName) {
        this.name = mTxtName;
    }

    public int getWaitingCnt() {
        return waitingCnt;
    }

    public void setWaitingCnt(int mTxtWaitingCnt) {
        this.waitingCnt = mTxtWaitingCnt;
    }

    public String getLocDetail() {
        return locDetail;
    }

    public void setLocDetail(String mTxtLocDetail) {
        this.locDetail = mTxtLocDetail;
    }
}
