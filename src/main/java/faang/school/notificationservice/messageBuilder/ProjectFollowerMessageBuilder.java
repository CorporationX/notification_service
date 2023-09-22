package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.dto.redis.ProjectFollowerEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProjectFollowerMessageBuilder implements MessageBuilder<ProjectFollowerEventDto, Locale> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(ProjectFollowerEventDto event, Locale locale) {
        String messagePattern = messageSource.getMessage("projectFollower.new", null, locale);
        return String.format(messagePattern, event.getSubscriberId());
    }
}
