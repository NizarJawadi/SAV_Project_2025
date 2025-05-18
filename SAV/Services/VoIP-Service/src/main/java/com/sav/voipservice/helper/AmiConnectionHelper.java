package com.sav.voipservice.helper;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;

public class AmiConnectionHelper {

    public static boolean isConnected(ManagerConnection connection) {
        return connection.getState() == ManagerConnectionState.CONNECTED;
    }

    public static boolean safeLogin(ManagerConnection connection) {
        try {
            if (!isConnected(connection)) {
                connection.login();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}