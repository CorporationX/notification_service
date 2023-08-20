package faang.school.notificationservice.messaging.builders;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public interface MessageBuilder<T> {

    String buildMessage(Locale local, T event);
}
