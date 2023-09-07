package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementMessageBuilder<T> implements MessageBuilder<AchievementEventDto> {

    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public String buildMessage(AchievementEventDto event, Locale locale) {
        String followerName = userServiceClient.getUserInternal(event.getReceiverId()).username();
        return messageSource.getMessage("achievement.new", new Object[]{followerName, event.getEventType()}, locale);
    }

    @Override
    public boolean supportsEventType(Class<?> eventType) {
        return eventType == AchievementEventDto.class;
    }
}
