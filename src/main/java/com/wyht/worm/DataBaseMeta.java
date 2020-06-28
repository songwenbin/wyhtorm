package com.wyht.worm;

public class DataBaseMeta {

    public static String CR = "\n";

    /*
    public String[] getReservedWords() {
        return new String[] { "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE", "BETWEEN",
                "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK",
                "COLLATE", "COLUMN", "CONDITION", "CONNECTION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS",
                "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES",
                "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED",
                "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH",
                "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FOR", "FORCE",
                "FOREIGN", "FROM", "FULLTEXT", "GOTO", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND",
                "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE", "INSERT",
                "INT", "INTEGER", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT",
                "LIKE", "LIMIT", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCATE", "LOCK", "LONG", "LONGBLOB", "LONGTEXT",
                "LOOP", "LOW_PRIORITY", "MATCH", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND",
                "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC", "ON", "OPTIMIZE",
                "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "POSITION", "PRECISION", "PRIMARY", "PROCEDURE",
                "PURGE", "READ", "READS", "REAL", "REFERENCES", "REGEXP", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESTRICT",
                "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE",
                "SEPARATOR", "SET", "SHOW", "SMALLINT", "SONAME", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE",
                "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STRAIGHT_JOIN",
                "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO",
                "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME",
                "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "WHEN", "WHERE", "WHILE", "WITH",
                "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL" };
    }

    String getFieldDefinition(ValueMetaInterface v, String tk, String pk, boolean useAutoinc, boolean addFieldName, boolean addCr ) {
        String retval = "";

        String fieldname = v.getName();
        if ( v.getLength() == DatabaseMeta.CLOB_LENGTH ) {
            v.setLength( getMaxTextFieldLength() );
        }
        int length = v.getLength();
        int precision = v.getPrecision();

        if ( addFieldName ) {
            retval += fieldname + " ";
        }

        int type = v.getType();
        switch ( type ) {
            case ValueMetaInterface.TYPE_TIMESTAMP:
            case ValueMetaInterface.TYPE_DATE:
                retval += "DATETIME";
                break;
            case ValueMetaInterface.TYPE_BOOLEAN:
                if ( supportsBooleanDataType() ) {
                    retval += "BOOLEAN";
                } else {
                    retval += "CHAR(1)";
                }
                break;

            case ValueMetaInterface.TYPE_NUMBER:
            case ValueMetaInterface.TYPE_INTEGER:
            case ValueMetaInterface.TYPE_BIGNUMBER:
                if ( fieldname.equalsIgnoreCase( tk ) || // Technical key
                        fieldname.equalsIgnoreCase( pk ) // Primary key
                ) {
                    if ( useAutoinc ) {
                        retval += "BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY";
                    } else {
                        retval += "BIGINT NOT NULL PRIMARY KEY";
                    }
                } else {
                    // Integer values...
                    if ( precision == 0 ) {
                        if ( length > 9 ) {
                            if ( length < 19 ) {
                                // can hold signed values between -9223372036854775808 and 9223372036854775807
                                // 18 significant digits
                                retval += "BIGINT";
                            } else {
                                retval += "DECIMAL(" + length + ")";
                            }
                        } else {
                            retval += "INT";
                        }
                    } else {
                        // Floating point values...
                        if ( length > 15 ) {
                            retval += "DECIMAL(" + length;
                            if ( precision > 0 ) {
                                retval += ", " + precision;
                            }
                            retval += ")";
                        } else {
                            // A double-precision floating-point number is accurate to approximately 15 decimal places.
                            // http://mysql.mirrors-r-us.net/doc/refman/5.1/en/numeric-type-overview.html
                            retval += "DOUBLE";
                        }
                    }
                }
                break;
            case ValueMetaInterface.TYPE_STRING:
                if ( length > 0 ) {
                    if ( length == 1 ) {
                        retval += "CHAR(1)";
                    } else if ( length < 256 ) {
                        retval += "VARCHAR(" + length + ")";
                    } else if ( length < 65536 ) {
                        retval += "TEXT";
                    } else if ( length < 16777216 ) {
                        retval += "MEDIUMTEXT";
                    } else {
                        retval += "LONGTEXT";
                    }
                } else {
                    retval += "TINYTEXT";
                }
                break;
            case ValueMetaInterface.TYPE_BINARY:
                retval += "LONGBLOB";
                break;
            default:
                retval += " UNKNOWN";
                break;
        }

        if ( addCr ) {
            retval += Const.CR;
        }

        return retval;
    }

    public String getStartQuote() {
        return "`";
    }

    public String getEndQuote() {
        return "`";
    }
    */

    public String getCreateTableStatement( String tableName, RowMeta fields, String tk,
                                           boolean useAutoinc, String pk, boolean semicolon ) {
        StringBuilder retval = new StringBuilder();
        retval.append( getCreateTableStatementHead() );

        retval.append(tableName).append(CR);

        /*
        retval.append( "(" ).append( CR );
        for ( int i = 0; i < fields.size(); i++ ) {
            if ( i > 0 ) {
                retval.append( ", " );
            } else {
                retval.append( "  " );
            }

            ValueMetaInterface v = fields.getValueMeta( i );
            retval.append( databaseMeta.getFieldDefinition( v, tk, pk, useAutoinc ) );
        }

        if ( tk != null ) {
            if ( databaseMeta.requiresCreateTablePrimaryKeyAppend() ) {
                retval.append( ", PRIMARY KEY (" ).append( tk ).append( ")" ).append( CR );
            }
        }

        // Primary keys
        if ( pk != null ) {
            if ( databaseMeta.requiresCreateTablePrimaryKeyAppend() ) {
                retval.append( ", PRIMARY KEY (" ).append( pk ).append( ")" ).append( Const.CR );
            }
        }
        retval.append( ")" ).append( Const.CR );

        retval.append( databaseMeta.getDatabaseInterface().getDataTablespaceDDL( variables, databaseMeta ) );
         */

        if ( semicolon ) {
            retval.append( ";" );
        }

        return retval.toString();
    }

    public String getCreateTableStatementHead() {
        return "CREATE TABLE ";
    }
}
