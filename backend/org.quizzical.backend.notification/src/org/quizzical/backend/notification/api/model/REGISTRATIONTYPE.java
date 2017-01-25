package org.quizzical.backend.notification.api.model;


public enum REGISTRATIONTYPE {
	CUSTOM("CUSTOM"),
	NONCUSTOM("NONCUSTOM");
	
    private final String value;

    REGISTRATIONTYPE(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static REGISTRATIONTYPE fromValue(String v) {
        for (REGISTRATIONTYPE c: REGISTRATIONTYPE.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}
