package com.example.xyzhang.testapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingyu on 2018/3/3.
 */

public class Font implements Serializable {
    private int id;
    private String name;
    private String userphone;
    private boolean finished;
    private double progress;
    private boolean downloaded;



    public int getId() {
        return id;
    }

    public String getUserphone() {
        return userphone;
    }

    public String getName() {
        return name;
    }

    public boolean isFinished() {
        return finished;
    }

    public double getProgress() {
        return progress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }
}
