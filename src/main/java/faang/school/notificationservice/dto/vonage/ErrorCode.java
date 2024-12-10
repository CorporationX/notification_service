package faang.school.notificationservice.dto.vonage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    DELIVERED(0),
    UKNOWN(1),
    ABSENT_SUBSCRIBER(2),
    HANDSET_BUSY(7),
    NETWORK_ERROR(8);

    private final int value;

    public boolean isSame(int value) {
        return this.value == value;
    }

    public static boolean isRetryable(ErrorCode errorCode) {
        return errorCode.getValue() == 2 || errorCode.getValue() == 7 || errorCode.getValue() == 8;
    }

    public static ErrorCode fromValue(int otherValue) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.isSame(otherValue)) {
                return errorCode;
            }
        }
        throw new IllegalArgumentException("Invalid error code: " + otherValue);
    }
}
