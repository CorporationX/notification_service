package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.NotificationData;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class ProjectFollowerMessageBuilder implements MessageBuilder<ProjectFollowerEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return ProjectFollowerEvent.class;
    }

    @Override
    public String buildMessage(ProjectFollowerEvent event, Locale locale, NotificationData notificationData) {
        return messageSource.getMessage("project-follower.new", new Object[]{notificationData.getFollower()}, locale);
    }
}