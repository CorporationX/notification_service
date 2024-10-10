package faang.school.notificationservice.messaging;

import java.util.Locale;

public interface MessageBuilder<E, M> {

    Class<E> getInstance();

    M buildMessage(E event, Locale locale);
}
