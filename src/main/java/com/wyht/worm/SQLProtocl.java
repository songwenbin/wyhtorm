package com.wyht.worm;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SQLProtocl implements Protocol{
    @Override
    public String insert(MetaModel metaModel, Map<String, Object> attributes, String... replacements) {
        return null;
    }

    protected void appendEmptyRow(MetaModel metaModel, StringBuilder query) {
        query.append("DEFAULT VALUES");
    }

    protected void appendQuestions(StringBuilder query, int count) {
        joinAndRepeat(query, "?", ", ", count);
    }

    public static void joinAndRepeat(StringBuilder sb, String str, String delimiter, int count) {
        if (count > 0) {
            sb.append(str);
            for (int i = 1; i < count; i++) {
                sb.append(delimiter);
                sb.append(str);
            }
        }
    }

    @Override
    public String insert(MetaModel metaModel, List<String> columns) {
        StringBuilder query = new StringBuilder().append("INSERT INTO ").append(metaModel.getTableName()).append(' ');
        if (columns.isEmpty()) {
            appendEmptyRow(metaModel, query);
        } else {
            query.append('(');
            join(query, columns, ", ");
            query.append(") VALUES (");
            appendQuestions(query, columns.size());
            query.append(')');
        }
        return query.toString();
    }

    private void join(StringBuilder sb, List<String> collection, String delimiter) {
        if (collection.isEmpty()) { return; }
        Iterator<?> it = collection.iterator();
        sb.append(it.next());
        while (it.hasNext()) {
            sb.append(delimiter);
            sb.append(it.next());
        }
    }
}
