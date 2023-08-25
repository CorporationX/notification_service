package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementMessageBuilder implements MessageBuilder {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto userDto, EventDto eventDto) {
        String message = messageSource.getMessage("achievement.new", new String[]{userDto.getUsername()}, userDto.getLocale());
        log.info("Message for achievement notification for user:{} has built successfully", userDto.getUsername());
        return message;
    }

    public EventType getEventType() {
        return EventType.ACHIEVEMENT_EVENT;
    }
}
