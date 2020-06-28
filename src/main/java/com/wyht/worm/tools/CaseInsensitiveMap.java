package com.wyht.dataengine.tools;

import java.util.Map;
import java.util.TreeMap;

public class CaseInsensitiveMap<V> extends TreeMap<String, V> {

    public CaseInsensitiveMap() {
        super(String.CASE_INSENSITIVE_ORDER);
    }

    public CaseInsensitiveMap(Map<? extends String, V> m) {
        this();
        putAll(m);
    }
}
