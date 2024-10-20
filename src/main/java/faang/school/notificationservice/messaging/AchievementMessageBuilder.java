package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.dto.UserDto;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class AchievementMessageBuilder extends MessageBuilder<AchievementEvent> {

    public AchievementMessageBuilder(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Class<AchievementEvent> getInstance() {
        return AchievementEvent.class;
    }

    @Override
    public String buildMessage(UserDto userDto, AchievementEvent event) {
        Object[] args = {userDto.getUsername(), event.getAchievementTitle()};
        return messageSource.getMessage("achievement.new", args, userDto.getLocale());
    }
}
