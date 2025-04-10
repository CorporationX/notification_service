package faang.school.notificationservice.exception;

public class PreferenceNotFountException extends CustomException {

    public PreferenceNotFountException(ExceptionMessage message, long userId) {
        super(message, userId);
    }
}
