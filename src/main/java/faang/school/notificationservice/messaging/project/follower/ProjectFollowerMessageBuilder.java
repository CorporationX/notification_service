package faang.school.notificationservice.messaging.project.follower;

import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProjectFollowerMessageBuilder implements MessageBuilder<ProjectFollowerEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return ProjectFollowerEvent.class;
    }

    @Override
    public String buildMessage(ProjectFollowerEvent event, Locale locale, Object[] args) {
        return messageSource.getMessage("project_follower.new", args, locale);
    }
}
