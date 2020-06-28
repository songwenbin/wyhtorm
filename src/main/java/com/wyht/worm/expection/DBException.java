package com.wyht.dataengine.expection;

public class DBException extends RuntimeException{

    public DBException() {
        super();
    }

    public DBException(Throwable cause) {
        super(cause);

    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     *
     * @param query SQL query
     * @param params - array of parameters, can be null
     * @param cause real cause.
     */
    public DBException(String query, Object[] params, Throwable cause) {
        this(getMessage(query, params, cause), cause);
    }

    private static String getMessage(String query, Object[] params, Throwable cause){

        StringBuilder sb = new StringBuilder(cause.toString()).append(", query: ").append(query);
        if (params != null && params.length > 0) {
            sb.append(", params: ");
            join(sb, params, ", ");
        }
        return sb.toString();
    }

    public static void join(StringBuilder sb, Object[] array, String delimiter) {
        if (empty(array)) { return; }
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append(delimiter);
            sb.append(array[i]);
        }
    }

    public static boolean empty(Object[] array) {
        return array == null || array.length == 0;
    }
}
