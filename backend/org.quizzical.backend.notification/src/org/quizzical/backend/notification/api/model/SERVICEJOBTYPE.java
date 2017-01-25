package org.quizzical.backend.notification.api.model;


public enum SERVICEJOBTYPE {
	ETL("ETL"),
	REPORTING("REPORTING");
    private final String value;

    SERVICEJOBTYPE(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SERVICEJOBTYPE fromValue(String v) {
        for (SERVICEJOBTYPE c: SERVICEJOBTYPE.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}
