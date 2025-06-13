package faang.school.notificationservice.exception;

public class FetchViaFeignException extends RuntimeException {
    public FetchViaFeignException(Long userId) {
        super("Failed to fetch user details for user ID: " + userId);
    }
}
