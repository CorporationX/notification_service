package faang.school.notificationservice.error;

public class MissingUserHeaderException extends RuntimeException {
    public MissingUserHeaderException(String msg) { super(msg); }
}