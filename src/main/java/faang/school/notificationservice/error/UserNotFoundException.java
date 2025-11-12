package faang.school.notificationservice.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long userId) {
        super("User " + userId + " not found");
    }
}