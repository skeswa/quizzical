package org.quizzical.backend.notification.api.model;


public enum NOTIFICATIONSTATUS {
	NEW("NEW"),
	SENT("SENT"),
	FAILED("FAILED");
    private final String value;

    NOTIFICATIONSTATUS(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NOTIFICATIONSTATUS fromValue(String v) {
        for (NOTIFICATIONSTATUS c: NOTIFICATIONSTATUS.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}
