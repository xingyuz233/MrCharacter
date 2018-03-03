package com.example.xyzhang.testapp;

/*
  Created by XY Zhang on 2018/2/22.
 */

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{
    private int id;
    private String phoneNumber;
    private String password;
    private String userName;
    public static String PHONENUMBER = "phonenumber";
    public static String PASSWORD = "password";
    public static String USERNAME = "username";

    public User(String phoneNumber, String password, String userName){
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }


}