package org.quizzical.backend.notification.api.model;


public enum AUDITTYPE {
	REPORTRECORDSIZE("REPORTRECORDSIZE"),
	REPORTING("REPORTING");
    private final String value;

    AUDITTYPE(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AUDITTYPE fromValue(String v) {
        for (AUDITTYPE c: AUDITTYPE.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}
