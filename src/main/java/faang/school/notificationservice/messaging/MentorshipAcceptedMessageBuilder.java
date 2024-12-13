package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MentorshipAcceptedMessageBuilder implements MessageBuilder<MentorshipAcceptedEvent>{
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return MentorshipAcceptedEvent.class ;
    }

    @Override
    public String buildMessage(MentorshipAcceptedEvent event, Locale locale) {
        Object[] placeholders = {event.getReceiverUserName(), event.getDescription()};
        return messageSource.getMessage("mentorship_accepted.new", placeholders, locale);
    }
}
