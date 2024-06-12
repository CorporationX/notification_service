package faang.school.notificationservice.builder;

import java.util.List;
import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(Locale locale, List<Object> messageArgs);

    Class<?> getEventType();
}
