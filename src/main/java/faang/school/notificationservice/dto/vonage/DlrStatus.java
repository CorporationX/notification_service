package faang.school.notificationservice.dto.vonage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DlrStatus {
    ACCEPTED("accepted"),
    DELIVERED("delivered"),
    BUFFERED("buffered"),
    EXPIRED("expired"),
    FAILED("failed"),
    REJECTED("rejected"),
    UNKNOWN("unknown");

    private final String name;

    public boolean isSame(String otherName) {
        return this.name.equals(otherName);
    }
}
