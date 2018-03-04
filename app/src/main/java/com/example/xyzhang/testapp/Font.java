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
    private int userid;
    private boolean finished;


    public int getId() {
        return id;
    }

    public int getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
