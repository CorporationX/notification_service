package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.messaging.ProjectFollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProjectFollowerBuilder implements MessageBuilder<ProjectFollowerEvent> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return ProjectFollowerEvent.class;
    }

    @Override
    public String buildMessage(ProjectFollowerEvent event, Locale locale) {
        return messageSource.getMessage(
                "follower.new",
                new Object[]{event.getFollowerId(), event.getProjectId()},
                locale
        );
    }
}
