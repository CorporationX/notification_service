package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PregerredContactNotification {
    EMAIL("EMAIL"),
    SMS("SMS");

    private final String value;

    PregerredContactNotification(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PregerredContactNotification fromValue(String value) {
        for (PregerredContactNotification preference : values()) {
            if (preference.value.equalsIgnoreCase(value)) {
                return preference;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
