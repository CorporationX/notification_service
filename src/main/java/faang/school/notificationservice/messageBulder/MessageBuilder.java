package faang.school.notificationservice.messageBulder;

import java.util.Locale;

public interface MessageBuilder <T>{

     String buildMessage(T event, Locale locale);
     Boolean supportsEventType(T eventType);

}
