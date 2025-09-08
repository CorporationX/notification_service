package faang.school.notificationservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String chatId) {
        super("user to chatId: " + chatId + ". Not found");
    }
}
