package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AchievementMessageBuilder implements MessageBuilder<AchievementEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient serviceClient;

    @Override
    public Class<AchievementMessageBuilder> getInstance() {
        return AchievementMessageBuilder.class;
    }

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {
        UserDto user = serviceClient.getUser(event.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", locale);
        String formattedDate = event.getReceivedAt().format(formatter);

        return messageSource.getMessage(
                "achievement.unlocked",
                new Object[]{
                        user.getUsername(),
                        event.getAchievementTitle(),
                        formattedDate
                },
                locale
        );
    }
}