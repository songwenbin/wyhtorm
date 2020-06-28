package com.wyht.worm;

import lombok.extern.slf4j.Slf4j;
import com.wyht.dataengine.expection.DBException;
import com.wyht.dataengine.expection.InitException;

import java.sql.*;
import java.util.Properties;

import static com.wyht.worm.RowProcessor.closeQuietly;

/**
 * 底层数据库的驱动类
 *
 * @author songwenbin
 */

@Slf4j
public class DB {
    private final String name;

    public DB(String name) {
        this.name = name;
    }

    public DB open(String driver, String url, String user, String password, Properties properties) {
        // 如果使用连接池，可以使用Connection连接器
        checkExistingConnection(name);
        try {
            Class.forName(driver);
            Connection connection;
            connection = properties == null ? DriverManager.getConnection(url, user, password)
                    : DriverManager.getConnection(url, properties);
            log.debug("Opened connection: {}, URL: {}", connection, url);
            // 如果使用连接池，可以使用Connection连接器
            ConnectionsAccess.attach(name, connection, url);
            return this;
        } catch (Exception e) {
            throw new InitException("Failed to connect to JDBC URL: " + url + " with user: " + user, e);
        }
    }

    private void checkExistingConnection(String name) {
        if (null != ConnectionsAccess.getConnection(name)) {
            throw new DBException("Cannot open a new connection because existing connection is still on current thread, name: " + name + ", connection instance: " + connection()
                    + ". This might indicate a logical error in your application.");
        }
    }

    public Connection connection() {
        Connection connection = ConnectionsAccess.getConnection(name);
        if (connection == null) {
            throw new DBException("there is no connection '" + name + "' on this thread, are you sure you opened it?");
        }
        return connection;
    }

    public int exec(String query) {
        long start = System.currentTimeMillis();
        Statement s = null;
        try {
            s = connection().createStatement();
            return s.executeUpdate(query);
        } catch (SQLException e) {
            throw new DBException(query, null, e);
        } finally {
            closeQuietly(s);
        }
    }

    Object execInsert(String query, String autoIncrementColumnName, Object... params) {
        System.out.println(query);
        /*
        if (!INSERT_PATTERN.matcher(query).find())
            throw new IllegalArgumentException("this method is only for inserts");

         */

        PreparedStatement ps;
        try {
            Connection connection = connection();
            /*
            ps = StatementCache.instance().getPreparedStatement(connection, query);
            if(ps == null){
                ps = connection.prepareStatement(query, new String[]{autoIncrementColumnName});
                StatementCache.instance().cache(connection, query, ps);
            }
             */
            ps = connection.prepareStatement(query, new String[]{autoIncrementColumnName});
            for (int index = 0; index < params.length;) {
                Object param = params[index++];
                if (param instanceof byte[]) {
                    byte[] bytes = (byte[]) param;
                    try {
                        Blob blob = connection.createBlob();
                        if (blob == null) { // SQLite
                            ps.setBytes(index, bytes);
                        } else {
                            blob.setBytes(1, bytes);
                            ps.setBlob(index, blob);
                        }
                    } catch (AbstractMethodError | SQLException e) {// net.sourceforge.jtds.jdbc.ConnectionJDBC2.createBlob is abstract :)
                        ps.setObject(index, param);
                    }
                } else {
                    ps.setObject(index, param);
                }
            }

            if (ps.executeUpdate() != 1) {
                return null;
            }

            ResultSet rs = null;
            try{
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    Object id = rs.getObject(1);
                    //LogFilter.logQuery(LOGGER, query, params, start);
                    return id;
                } else {
                    return -1;
                }
            } catch (SQLException e) {
                log.error("Failed to find out the auto-incremented value, returning -1, query: {}", query, e);
                return -1;
            } finally {
                closeQuietly(rs);
            }
        } catch (SQLException e) {
            throw new DBException(query, params, e);
        } finally {
            // don't close ps as it could have come from the cache!
        }
    }

    public void openTransaction() {
        try {
            Connection c = ConnectionsAccess.getConnection(name);
            if (c == null) {
                throw new DBException("Cannot open transaction, connection '" + name + "' not available");
            }
            c.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    public void commitTransaction() {
        try {
            Connection c = ConnectionsAccess.getConnection(name);
            if (c == null) {
                throw new DBException("Cannot commit transaction, connection '" + name + "' not available");
            }
            c.commit();
        } catch (SQLException ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    public void rollbackTransaction() {
        try {
            Connection c = ConnectionsAccess.getConnection(name);
            if (c == null) {
                throw new DBException("Cannot rollback transaction, connection '" + name + "' not available");
            }
            c.rollback();
        } catch (SQLException ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    public boolean hasConnection() {
        return null != ConnectionsAccess.getConnection(name);
    }

    public Connection getConnection() {
        return connection();
    }

    public void closeConnection() {

    }
}
