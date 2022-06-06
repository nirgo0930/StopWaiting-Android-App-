package com.example.stopwaiting.dto;

import android.net.Uri;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaitingListItem implements Serializable {
    private Uri imgUri;
    private String name;
    private Long qId;
    private Long wId;
    private int waitingCnt;
    private String locDetail;
    private String imgUrl;

    public WaitingListItem(Uri imgId, String txtName, Long qId, Long wId, int txtWaitingCnt, String txtLocDetail) {
        this.imgUri = imgId;
        this.name = txtName;
        this.qId = qId;
        this.wId = wId;
        this.waitingCnt = txtWaitingCnt;
        this.locDetail = txtLocDetail;
    }

    public WaitingListItem(String imgId, String txtName, Long qId, Long wId, int txtWaitingCnt, String txtLocDetail) {
        this.imgUrl = imgId;
        this.name = txtName;
        this.qId = qId;
        this.wId = wId;
        this.waitingCnt = txtWaitingCnt;
        this.locDetail = txtLocDetail;
    }

}
