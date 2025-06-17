package faang.school.notificationservice.exception;

public class UserEmailMissingException extends RuntimeException {
    public UserEmailMissingException(Long userId) {
        super("User email is missing for userId = " + userId);
    }
}
