package faang.school.notificationservice.dto.events;

public enum RequestStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    CANCELLED,
    FAILED;

    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }
}