package faang.school.notificationservice.messageBuilder;

import java.util.Locale;

public interface MessageBuilder {

    String buildMessage(Locale locale, String userName);
}
