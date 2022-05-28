package com.example.stopwaiting.dto;

import android.net.Uri;

import java.io.Serializable;

public class WaitingListItem implements Serializable {
    private Uri imgUri;
    private String name;
    private Long qId;
    private int waitingCnt;
    private String locDetail;
    private String imgUrl;

    public WaitingListItem() {

    }

    public WaitingListItem(Uri imgId, String txtName, Long qId, int txtWaitingCnt, String txtLocDetail) {
        this.imgUri = imgId;
        this.name = txtName;
        this.qId = qId;
        this.waitingCnt = txtWaitingCnt;
        this.locDetail = txtLocDetail;
    }

    public WaitingListItem(String imgId, String txtName, Long qId, int txtWaitingCnt, String txtLocDetail) {
        this.imgUrl = imgId;
        this.name = txtName;
        this.qId = qId;
        this.waitingCnt = txtWaitingCnt;
        this.locDetail = txtLocDetail;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri mImgUri) {
        this.imgUri = mImgUri;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String mTxtName) {
        this.name = mTxtName;
    }

    public Long getQId() {
        return qId;
    }

    public void setQId(Long qId) {
        this.qId = qId;
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
