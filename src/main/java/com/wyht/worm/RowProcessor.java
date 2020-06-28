package com.wyht.worm;

import com.wyht.dataengine.expection.DBException;
import com.wyht.dataengine.tools.CaseInsensitiveMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class RowProcessor {

    public enum ResultSetType{
        FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY),
        SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE),
        SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE);

        private int value;

        ResultSetType(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ResultSetConcur{

        READ_ONLY(ResultSet.CONCUR_READ_ONLY),
        UPDATABLE(ResultSet.CONCUR_UPDATABLE);

        private int value;

        ResultSetConcur(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final ResultSet rs;
    private final Statement s;

    protected RowProcessor(ResultSet rs, Statement s){
        this.rs = rs;
        this.s = s;
    }

    public void with(RowListener listener){
        try {
            processRS(listener);
        } catch(SQLException e) {
            throw new DBException(e);
        } finally {
            closeQuietly(rs);
            closeQuietly(s);
        }
    }

    protected void processRS(RowListener listener) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        String[] labels = new String[metaData.getColumnCount()];
        int i = 0;
        while (i < labels.length) {
            labels[i++] = metaData.getColumnLabel(i);
        }

        while (rs.next()) {
            Map<String, Object> row = new CaseInsensitiveMap<>();
            i = 0;
            while (i < labels.length) {
                row.put(labels[i++], rs.getObject(i));
            }
            if (!listener.next(row)) { break; }
        }
    }

    public static void closeQuietly(AutoCloseable autoCloseable) {
        try {
            if (autoCloseable != null) { autoCloseable.close(); }
        } catch (Exception ignore) {}
    }


}
