package faang.school.notificationservice.builder;

public interface EventBuilder<T> {
    public T build(String message);
}
