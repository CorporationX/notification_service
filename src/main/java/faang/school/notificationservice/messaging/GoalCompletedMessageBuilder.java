package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.GoalCompletedEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder<GoalCompletedEventDto> {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return GoalCompletedEventDto.class;
    }

    @Override
    public String buildMessage(GoalCompletedEventDto event, Locale locale) {
        log.info("Received a request to build a message, {}", "goalcompleted.message");
        return messageSource.getMessage(
                "goalcompleted.message",
                new Object[] {event.getCompletedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", locale))},
                locale);
    }
}
