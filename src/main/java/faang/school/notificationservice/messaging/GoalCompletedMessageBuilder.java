package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.GoalCompletedEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalCompletedMessageBuilder implements MessageBuilder {
    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return GoalCompletedMessageBuilder.class;
    }

    @Override
    public String buildMessage(Object event, Locale locale) {
        GoalCompletedEventDto goalEvent = (GoalCompletedEventDto) event;
        log.info("Received a request to build a message, {}", "goalcompleted.message");
        String eventDate = goalEvent.getCompletedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", locale));
        String result = messageSource.getMessage(
                "goalcompleted.message",
                new Object[]{eventDate},
                locale
        );
        return result;
    }
}
