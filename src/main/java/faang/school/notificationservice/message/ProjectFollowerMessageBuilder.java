package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.ProjectFollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProjectFollowerMessageBuilder implements MessageBuilder {
    private final MessageSource messageSource;
    @Override
    public Class<?> getInstance() {
        return ProjectFollowerEvent.class;
    }

    @Override
    public String buildMessage(Object event, Locale locale) {
        return messageSource.getMessage("follower.new", new Object[]{}, locale);
    }
}
