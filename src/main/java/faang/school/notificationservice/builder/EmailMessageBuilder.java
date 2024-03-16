package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EmailEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
@Component
@RequiredArgsConstructor
public class EmailMessageBuilder implements MessageBuilder<EmailEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    @Override
    public String buildMessage(EmailEvent event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.getUserId());
        return messageSource.getMessage("premium.new", new Object[]{user.getUsername()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return EmailEvent.class;
    }
}
