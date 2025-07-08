package faang.school.notificationservice.messaging.subscription;

import faang.school.notificationservice.event.kafka.ViewProfileEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ViewProfileMessageBuilder implements MessageBuilder<ViewProfileEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(ViewProfileEvent event, Locale locale) {
        String name = event.getFollower().getUsername();
        String message = "view.profile";
        return messageSource.getMessage(message, new Object[]{name}, locale);
    }

    @Override
    public Class<?> getInstance() {
        return ViewProfileEvent.class;
    }
}
