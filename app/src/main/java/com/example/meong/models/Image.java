package com.example.meong.models;

import androidx.annotation.Nullable;

import com.google.gson.internal.LinkedTreeMap;

public class Image {
    private String url;
    private LinkedTreeMap src;

    public String getUrl() {
        return url;
    }

    public LinkedTreeMap getSrc() {
        return src;
    }
}
