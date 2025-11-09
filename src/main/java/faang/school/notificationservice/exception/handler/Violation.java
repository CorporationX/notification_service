package faang.school.notificationservice.exception.handler;

public record Violation(
        String fieldName,
        String message
) {
}