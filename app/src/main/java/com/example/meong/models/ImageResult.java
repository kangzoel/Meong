package com.example.meong.models;

import java.util.List;

public class ImageResult {
    private String url;
    private List<Image> photos;

    public List<Image> getPhotos() {
        return photos;
    }

    public String getUrl() {
        return url;
    }
}
