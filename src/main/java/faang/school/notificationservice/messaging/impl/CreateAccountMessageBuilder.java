package faang.school.notificationservice.messaging.impl;

import faang.school.notificationservice.event.account.CreateAccountEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CreateAccountMessageBuilder implements MessageBuilder<CreateAccountEvent> {

    private static final String MESSAGE_KEY = "account.created";

    private final MessageSource messageSource;

    @Override
    public Class<CreateAccountEvent> supportEventType() {
        return CreateAccountEvent.class;
    }

    @Override
    public String buildMessage(CreateAccountEvent event, Locale locale) {
        return messageSource.getMessage(MESSAGE_KEY, new Object[]{
                event.getAccountType(), event.getCurrency()}, locale);
    }
}
