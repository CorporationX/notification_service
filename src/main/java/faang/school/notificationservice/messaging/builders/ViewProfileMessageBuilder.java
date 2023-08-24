package faang.school.notificationservice.messaging.builders;

import faang.school.notificationservice.massages.MessageBuilder;
import faang.school.notificationservice.messaging.events.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ViewProfileMessageBuilder<T> implements MessageBuilder<ProfileViewEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProfileViewEvent event, Locale locale) {
        return messageSource.getMessage("visitor.new",
                new Object[]{event.getIdVisitor(), event.getIdVisited()}, locale);
    }

    @Override
    public boolean supportsEventType(Object eventType) {
        return eventType instanceof ProfileViewEvent;
    }

}
