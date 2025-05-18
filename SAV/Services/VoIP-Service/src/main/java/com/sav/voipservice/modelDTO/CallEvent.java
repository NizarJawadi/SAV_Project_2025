package com.sav.voipservice.modelDTO;


public class CallEvent {

    private String eventType;
    private String timestamp;

    // Constructeur
    public CallEvent() {}

    // Getters et setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
