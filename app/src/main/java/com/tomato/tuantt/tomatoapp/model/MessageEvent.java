package com.tomato.tuantt.tomatoapp.model;

public class MessageEvent {
    private Event event;

    private int id;

    public MessageEvent(Event event, int id) {
        this.event = event;
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public int getId() {
        return id;
    }
}
