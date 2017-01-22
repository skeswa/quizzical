package org.quizzical.backend.gogo.collection;

import java.util.List;

public class CollectionCommands {
    public static final String SCOPE = "coll";
    public static final String[] FUNCTIONS = { "first", "rest" };

    /** Returns the first object in a list. */
    public Object first(List<?> list) {
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /** Returns the rest of the list, meaning everything but the first object. */
    public List<?> rest(List<?> list) {
        if (list != null && !list.isEmpty() && list.size() > 1) {
            return list.subList(1, list.size());
        }
        return null;
    }
}
