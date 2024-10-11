package faang.school.notificationservice.messaging;

import java.util.Locale;

public interface MessageBuilder<E> {

    Class<E> getInstance();

    String buildMessage(E event, Locale locale);
}
