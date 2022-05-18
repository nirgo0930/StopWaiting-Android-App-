package com.example.stopwaiting.DTO;

import android.net.Uri;

import java.io.Serializable;

public class ImgItem implements Serializable {
    private String name;
    private Long id;
    private String uri;

    public ImgItem() {

    }

    public ImgItem(String name, Long id, Uri uri) {
        this.name = name;
        this.id = id;
        this.uri = uri.toString();
    }

    public ImgItem(String name, Long id, String uri) {
        this.name = name;
        this.id = id;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSUri() {
        return uri;
    }

    public void setSUri(String uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return Uri.parse(uri);
    }

    public void setUri(Uri uri) {
        this.uri = uri.toString();
    }
}
