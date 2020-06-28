package com.wyht.worm;

import java.util.Map;

public interface RowListener {

    /**
     * Implementations of this interface can return "false" from the next() method in order to stop fetching more results from DB.
     * Immediately after returning "false", ActiveJDBC will close JDBC resources associated with this request:
     * Statement and ResultSet.
     *
     * @param row Map instance containing values for a row. Keys are names of columns and values are .. values.
     * @return false if this listener needs to stop processing (no more calls to this method)
     */
    boolean next(Map<String, Object> row);
}
