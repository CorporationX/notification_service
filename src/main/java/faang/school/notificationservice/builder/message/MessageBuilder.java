package faang.school.notificationservice.builder.message;

import java.util.Locale;

public interface MessageBuilder<T> {
    String build(T event, Locale locale);
}
