package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.event.ProjectFollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProjectFollowerMessageBuilder implements MessageBuilder<ProjectFollowerEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProjectFollowerEvent event, Locale locale) {
        return messageSource.getMessage("project.subscriber", null, locale);
    }
}
