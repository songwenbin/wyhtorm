package com.wyht.worm;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MetaModel {

    private Map<String, ColumnMetadata> columnMetadata;

    private String dbName;
    private String tableName;


    public Map<String, ColumnMetadata> getColumnMetadata() {
        return columnMetadata;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDBName() {
        return dbName;
    }

    public static MetaModel getMetaModel(String db, String tableName) {
        MetaModel metaModel = new MetaModel();
        metaModel.init(db, tableName);
        return metaModel;
    }

    void init(String dbName, String tableName) {
        this.dbName = dbName;
        this.tableName = tableName;
        try {
            Connection c = ConnectionsAccess.getConnection(dbName);
            if (c == null) {
                throw new com.wyht.dataengine.expection.DBException("Failed to retrieve metadata from DB, connection: '" + dbName + "' is not available");
            }
            DatabaseMetaData databaseMetaData = c.getMetaData();
            String dbType = c.getMetaData().getDatabaseProductName();

            columnMetadata = fetchMetaParams(databaseMetaData, dbType, tableName);
            /*
            registerColumnMetadata(table, metaParams);

            for (String table : tables) {
                discoverAssociationsFor(table, dbName);
            }
            processOverrides(modelClasses);
             */
        } catch (Exception e) {
            e.printStackTrace();
            /*
            //initedDbs.remove(dbName);
            if (e instanceof InitException) {
                throw (InitException) e;
            }
            if (e instanceof DBException) {
                throw (DBException) e;
            } else {
                throw new InitException(e);
            }

             */
        }
    }

    public Map<String, ColumnMetadata> fetchMetaParams(DatabaseMetaData databaseMetaData, String dbType, String table) throws SQLException {

        String schema = getConnectionSchema(databaseMetaData);
        String catalog = getConnectionCatalog(databaseMetaData);

        ResultSet rs = databaseMetaData.getColumns(catalog, schema, "t1", null);
        Map<String, ColumnMetadata> columns = getColumns(rs, dbType);
        rs.close();
        return columns;
    }

    private Map<String, ColumnMetadata> getColumns(ResultSet rs, String dbType) throws SQLException {
        Map<String, ColumnMetadata> columns = new HashMap<>();
        while (rs.next()) {
            ColumnMetadata cm = new ColumnMetadata(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"), rs.getInt("COLUMN_SIZE"));
            columns.put(cm.getColumnName(), cm);
        }
        return columns;
    }


        /*
        String[] parts = table.split("\\.", 3);

        String schema = null;

        String tableName = null;

        switch (parts.length) {
            case 1:
                schema = getConnectionSchema(databaseMetaData);
                tableName = parts[0];
                break;
            case 2:
                schema = parts[0];
                tableName = parts[1];
                break;
        }

        if (Util.blank(tableName) || (schema != null && schema.trim().length() == 0)) {
            throw new com.wyht.dataengine.expection.DBException("invalid table name : " + table);
        }

        String catalog = getConnectionCatalog(databaseMetaData);

        tableName = mangleTableName(tableName, dbType);

        ResultSet rs = databaseMetaData.getColumns(catalog, schema, tableName, null);
        Map<String, ColumnMetadata> columns = getColumns(rs, dbType);
        rs.close();

        //try upper case table name - Oracle uses upper case
        if (columns.isEmpty()) {
            rs = databaseMetaData.getColumns(catalog, schema, tableName.toUpperCase(), null);
            columns = getColumns(rs, dbType);
            rs.close();
        }

        //if upper case not found, try lower case.
        if (columns.isEmpty()) {
            rs = databaseMetaData.getColumns(catalog, schema, tableName.toLowerCase(), null);
            columns = getColumns(rs, dbType);
            rs.close();
        }

        if (columns.size() > 0) {
            LogFilter.log(LOGGER, LogLevel.INFO, "Fetched metadata for table: {}", table);
        } else {
            LogFilter.log(LOGGER, LogLevel.WARNING, "Failed to retrieve metadata for table: '{}'."
                            + " Are you sure this table exists? For some databases table names are case sensitive.",
                    table);
        }

        return columns;
    }
    */

    private String getConnectionSchema(DatabaseMetaData databaseMetaData) throws SQLException {
        try {
            return databaseMetaData.getConnection().getSchema();
        } catch (SQLException e) {
            throw e;
        } catch (Exception ignore) {
        } // getSchema does not exist on android.
        return null;
    }

    private String getConnectionCatalog(DatabaseMetaData databaseMetaData) throws SQLException {
        try {
            return databaseMetaData.getConnection().getCatalog();
        } catch (SQLException e) {
            throw e;
        } catch (Exception ignore) {
        } // getCatalog does not exist on android.
        return null;
    }
}
