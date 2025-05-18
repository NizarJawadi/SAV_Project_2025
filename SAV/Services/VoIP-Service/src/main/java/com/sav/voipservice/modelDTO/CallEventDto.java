package com.sav.voipservice.modelDTO;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.NewChannelEvent;

import java.util.Date;


@Getter
@ToString
public class CallEventDto {
    // Getters (nécessaires pour la sérialisation JSON)
    private String eventType;
    private String channel;
    private String callerIdNum;
    private String callerIdName;
    private String connectedLineNum;
    private String connectedLineName;
    private String uniqueId;
    private Date timestamp;
    private String dialStatus;
    private String destination;
    private Integer duration;
    private String cause;
    private String causeTxt;

    // Constructeur pour NewChannelEvent
    public CallEventDto(NewChannelEvent event) {
        this.eventType = "NewChannel";
        this.channel = event.getChannel();
        this.callerIdNum = event.getCallerIdNum();
        this.callerIdName = event.getCallerIdName();
        this.connectedLineNum = event.getConnectedLineNum();
        this.connectedLineName = event.getConnectedLineName();
        this.uniqueId = event.getUniqueId();
        this.timestamp = event.getDateReceived();
    }

    // Constructeur pour HangupEvent
    public CallEventDto(HangupEvent event) {
        this.eventType = "Hangup";
        this.channel = event.getChannel();
        this.callerIdNum = event.getCallerIdNum();
        this.callerIdName = event.getCallerIdName();
        this.connectedLineNum = event.getConnectedLineNum();
        this.connectedLineName = event.getConnectedLineName();
        this.uniqueId = event.getUniqueId();
        this.timestamp = event.getDateReceived();
    }

    // Constructeur pour DialEvent
    public CallEventDto(DialEvent event) {
        this.eventType = "Dial";
        this.channel = event.getChannel();
        this.callerIdNum = event.getCallerIdNum();
        this.callerIdName = event.getCallerIdName();
        this.connectedLineNum = event.getConnectedLineNum();
        this.connectedLineName = event.getConnectedLineName();
        this.uniqueId = event.getUniqueId();
        this.timestamp = event.getDateReceived();
        this.dialStatus = event.getDialStatus();
        this.destination = event.getDestination();

    }

}
