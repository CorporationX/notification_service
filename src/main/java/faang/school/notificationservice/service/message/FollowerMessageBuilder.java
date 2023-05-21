package faang.school.notificationservice.service.message;

import faang.school.notificationservice.dto.FollowerEvent;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class FollowerMessageBuilder extends MessageBuilder<FollowerEvent> {

    public FollowerMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public String getMessage(Locale locale, String... args) {
        return messageSource.getMessage("follower.new", args, locale);
    }

    @Override
    public Class<FollowerEvent> getEvent() {
        return FollowerEvent.class;
    }
}
