package com.example.xyzhang.testapp.util;

public class SessionID {
    private static SessionID ourInstance = new SessionID();

    private String id, user;

    public static SessionID getInstance() {
        return ourInstance;
    }

    private SessionID() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
