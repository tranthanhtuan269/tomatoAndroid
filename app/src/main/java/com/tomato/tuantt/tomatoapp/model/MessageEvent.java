package com.tomato.tuantt.tomatoapp.model;

import java.util.HashMap;

public class MessageEvent {
    private Event event;

    private int id;

    private HashMap hashMap;

    private String phoneNumber;

    public MessageEvent(Event event, int id, String phoneNumber) {
        this.event = event;
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public MessageEvent(Event event, int id, HashMap<String, String> params) {
        this.event = event;
        this.hashMap = params;
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public int getId() {
        return id;
    }

    public HashMap getHashMap() {
        return hashMap;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
