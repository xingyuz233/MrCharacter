package com.example.xyzhang.testapp.util;

public class SessionID {
    private static SessionID ourInstance = new SessionID();

    private String id;

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
}
