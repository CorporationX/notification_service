package faang.school.notificationservice.exception.feign;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}