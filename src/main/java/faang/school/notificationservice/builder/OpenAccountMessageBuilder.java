package faang.school.notificationservice.builder;

import faang.school.notificationservice.dto.OpenAccountEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class OpenAccountMessageBuilder implements MessageBuilder<OpenAccountEvent>{
    @Override
    public String buildMessage(OpenAccountEvent eventType, Locale locale) {
        return "You have successfully opened an account!!! The status of your account is " + eventType.getAccountStatus();
    }

    @Override
    public Class<?> supportsEventType() {
        return OpenAccountEvent.class;
    }
}
