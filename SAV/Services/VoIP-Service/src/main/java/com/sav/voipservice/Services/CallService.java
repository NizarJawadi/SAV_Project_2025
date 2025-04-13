package com.sav.voipservice.Services;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CallService {
    @Autowired
    private ManagerConnection managerConnection;

    public void makeCall(String from, String to) throws IOException, AuthenticationFailedException, TimeoutException {
        OriginateAction originateAction = new OriginateAction();
        originateAction.setChannel("SIP/" + from);
        originateAction.setContext("default");
        originateAction.setExten(to);
        originateAction.setPriority(1);
        managerConnection.login();
        managerConnection.sendAction(originateAction);
    }
}
