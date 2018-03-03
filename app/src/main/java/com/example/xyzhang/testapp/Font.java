package com.example.xyzhang.testapp;

import java.io.Serializable;

/**
 * Created by xingyu on 2018/3/3.
 */

public class Font implements Serializable {
    private int id;
    private String name;
    private int userid;

    public int getId() {
        return id;
    }

    public int getUserid() {
        return userid;
    }

    public String getName() {
        return name;
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

}
