package faang.school.notificationservice.exception;

public class FetchViaFeignException extends RuntimeException {
    public FetchViaFeignException(Long userId) {
        super(String.format("Failed to fetch user details for user ID: %d", userId));
    }
}
