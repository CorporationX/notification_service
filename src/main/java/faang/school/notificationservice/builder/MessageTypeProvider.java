package faang.school.notificationservice.builder;

public interface MessageTypeProvider<T> {
    Class<T> getInstance();
}