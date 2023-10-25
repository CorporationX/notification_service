package faang.school.notificationservice.message_builder;

import faang.school.notificationservice.dto.notification.NotificationData;
import faang.school.notificationservice.dto.redis.MentorshipAcceptedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipMessageBuilder implements MessageBuilder<MentorshipAcceptedEventDto> {
    private final MessageSource messageSource;

    public String getPredefinedMessage(NotificationData data, Locale locale) {
        return messageSource.getMessage("mentorship.accepted", new Object[]{data.getFrom()}, locale);
    }

    @Override
    public String buildMessage(NotificationData data, Locale locale) {
        return getPredefinedMessage(data, locale);
    }

    @Override
    public boolean supportsEventType(MentorshipAcceptedEventDto eventType) {
        return true;
    }
}
