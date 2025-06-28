package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.AchievementEvent;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class AchievementMessageBuilder implements MessageBuilder<AchievementEvent> {

    @Override
    public Class<AchievementMessageBuilder> getInstance() {
        return AchievementMessageBuilder.class;
    }

    @Override
    public String buildMessage(AchievementEvent event, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", locale);
        return String.format(
                locale,
                "Поздравляем! Вы получили достижение \"%s\"! Дата: %s",
                event.getAchievementTitle(),
                event.getReceivedAt().format(formatter)
        );
    }
}