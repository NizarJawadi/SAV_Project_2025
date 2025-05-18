package com.sav.voipservice.Services;

import com.sav.voipservice.modelDTO.CallEventDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AmiService {
    private static final Logger logger = LoggerFactory.getLogger(AmiService.class);
    private static final int RECONNECT_DELAY = 5; // seconds
    private static final int MAX_RECONNECT_ATTEMPTS = 3;

    private final ManagerConnection managerConnection;
    private final SimpMessagingTemplate messagingTemplate;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private int reconnectAttempts = 0;

    @Autowired
    public AmiService(ManagerConnection managerConnection,
                      SimpMessagingTemplate messagingTemplate) {
        this.managerConnection = managerConnection;
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void init() {
        connectWithRetry();
    }

    @Async
    public void connectWithRetry() {
        try {
            if (managerConnection.getState() != ManagerConnectionState.CONNECTED) {
                managerConnection.login();
                managerConnection.addEventListener(this::handleManagerEvent);
                reconnectAttempts = 0;
                logger.info("AMI connection established successfully");
                sendStatusUpdate(true);
            }
        } catch (Exception e) {
            logger.error("AMI connection failed (attempt {} of {})",
                    reconnectAttempts + 1, MAX_RECONNECT_ATTEMPTS, e);

            if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
                reconnectAttempts++;
                scheduler.schedule(this::connectWithRetry, RECONNECT_DELAY, TimeUnit.SECONDS);
            } else {
                logger.error("Maximum reconnection attempts reached");
                sendStatusUpdate(false);
            }
        }
    }

    private void handleManagerEvent(ManagerEvent event) {
        try {
            CallEventDto dto = null;

            if (event instanceof NewChannelEvent) {
                dto = new CallEventDto((NewChannelEvent) event);
                System.out.println(dto);
            } else if (event instanceof HangupEvent) {
                dto = new CallEventDto((HangupEvent) event);
                System.out.println(dto);
            } else if (event instanceof DialEvent) {
                dto = new CallEventDto((DialEvent) event);
                System.out.println(dto);
            }

            if (dto != null) {
                messagingTemplate.convertAndSend("/topic/ami-events", dto);

            }
        } catch (Exception e) {
            logger.error("Error processing AMI event", e);
        }
    }

    private void sendStatusUpdate(boolean connected) {
        messagingTemplate.convertAndSend("/topic/ami-status",
                Map.of("connected", connected, "timestamp", System.currentTimeMillis()));
    }

    @PreDestroy
    public void cleanup() {
        try {
            scheduler.shutdown();
            if (managerConnection.getState() == ManagerConnectionState.CONNECTED) {
                managerConnection.logoff();
                logger.info("AMI disconnected successfully");
                sendStatusUpdate(false);
            }
        } catch (Exception e) {
            logger.warn("Error during AMI disconnection", e);
        }
    }

    public boolean isConnected() {
        return managerConnection.getState() == ManagerConnectionState.CONNECTED;
    }
}