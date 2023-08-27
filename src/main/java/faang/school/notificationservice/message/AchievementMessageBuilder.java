package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.event.AchievementEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementMessageBuilder implements MessageBuilder<AchievementEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto userDto, AchievementEventDto eventDto) {
        String message = messageSource.getMessage("achievement.new",
                new String[]{userDto.getUsername(), eventDto.getAchievementTitle()}, userDto.getLocale());
        log.info("Message for achievement notification for user:{} has built successfully", userDto.getUsername());
        return message;
    }

    public Class<AchievementEventDto> getEventType() {
        return AchievementEventDto.class;
    }
}
