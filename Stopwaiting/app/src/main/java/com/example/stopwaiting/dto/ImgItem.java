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
public class ImgItem implements Serializable {
    private String name;
    private Long id;
    private String uri;

    public ImgItem(String name, Long id, Uri uri) {
        this.name = name;
        this.id = id;
        this.uri = uri.toString();
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
