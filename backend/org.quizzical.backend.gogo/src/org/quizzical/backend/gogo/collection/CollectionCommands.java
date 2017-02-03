package org.quizzical.backend.gogo.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class CollectionCommands {
    public static final String SCOPE = "coll";
    public static final String[] FUNCTIONS = { "first", "rest", "toList", "toLongList" };

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
    
    public List<String> toList(String commaDelimitedList) {
    	StringTokenizer st = new StringTokenizer(commaDelimitedList,",");
    	List<String> list = new ArrayList<>();
    	while (st.hasMoreTokens()) {
    		list.add(st.nextToken());
    	}
        return list;
    }
    
    public List<Long> toLongList(String commaDelimitedList) {
    	StringTokenizer st = new StringTokenizer(commaDelimitedList,",");
    	List<Long> list = new ArrayList<>();
    	while (st.hasMoreTokens()) {
    		list.add(Long.valueOf(st.nextToken()));
    	}
        return list;
    }
}
