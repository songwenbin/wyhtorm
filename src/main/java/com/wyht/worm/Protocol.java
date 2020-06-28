package com.wyht.worm;

import java.util.List;
import java.util.Map;

public interface Protocol {

    String insert(MetaModel metaModel, Map<String, Object> attributes, String ... replacements);

    String insert(MetaModel metaModel, List<String> columns);
}
