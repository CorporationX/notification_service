package faang.school.notificationservice.handler;

@FunctionalInterface
public interface ErrorHandler {
    String handle(Exception ex);
}
