package com.wyht.worm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private MetaModel metaModelLocal;
    List<Object> values = new ArrayList<>();
    private Map<String, Object> attributes;

    public Model(String DB, String tableName) {
        this.attributes = new HashMap<String, Object>();
        this.metaModelLocal = MetaModel.getMetaModel(DB, tableName);
    }

    public Model set(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public Boolean save() {
        boolean done;
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, ColumnMetadata> entry : metaModelLocal.getColumnMetadata().entrySet()) {
            columns.add(entry.getKey());
            values.add(attributes.get(entry.getKey()));
        }

        Protocol protocol = new SQLProtocl();
        String query = protocol.insert(metaModelLocal, columns);
        System.out.println(query);
        Object id = new DB(metaModelLocal.getDBName()).execInsert(query, metaModelLocal.getTableName(), values.toArray());

        done = (id != null);
        return done;
    }
}
