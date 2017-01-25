package org.quizzical.backend.notification.api.model;


public enum REGISTRATIONSTATUS {
	NEW("NEW"),
	APPROVED("APPROVED"),
	NOTIFIED("NOTIFIED"),
	ACCEPTED("ACCEPTED"),
	FINALIZED("FINALIZED");
    private final String value;

    REGISTRATIONSTATUS(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static REGISTRATIONSTATUS fromValue(String v) {
        for (REGISTRATIONSTATUS c: REGISTRATIONSTATUS.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}
