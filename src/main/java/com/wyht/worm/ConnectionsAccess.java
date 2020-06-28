package com.wyht.worm;

import lombok.extern.slf4j.Slf4j;
import com.wyht.dataengine.expection.InternalException;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConnectionsAccess {
    private static final ThreadLocal<HashMap<String, Connection>> connectionsTL = new ThreadLocal<>();

    public ConnectionsAccess() {

    }

    static Map<String, Connection> getConnectionMap() {
        if (connectionsTL.get() == null)
            connectionsTL.set(new HashMap<String, Connection>());
        return connectionsTL.get();
    }

    static Connection getConnection(String dbName) {
        return getConnectionMap().get(dbName);
    }

    static void attach(String dbName, Connection connection, String extraInfo) {
        if (ConnectionsAccess.getConnectionMap().get(dbName) != null) {
            throw new InternalException("You are opening a connection " + dbName + " without closing a previous one. Check your logic. Connection still remains on thread: " + ConnectionsAccess.getConnectionMap().get(dbName));
        }
        ConnectionsAccess.getConnectionMap().put(dbName, connection);
        log.debug("Attached connection named: {}: to current thread: {}. Extra info: {}", dbName, connection, extraInfo);
    }

    static void detach(String dbName) {
        log.debug("Detached connection named: {} from current thread: {}", dbName, getConnectionMap().get(dbName));
        getConnectionMap().remove(dbName);
    }
}
