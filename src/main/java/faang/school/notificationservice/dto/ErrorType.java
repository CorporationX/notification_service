package faang.school.notificationservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    PROCESSOR_NOT_FOUND("Required message builder was not found"),
    NOTIFICATOR_NOT_FOUND("Required notification service was not found")
    ;

    private final String errorMessage;
}
