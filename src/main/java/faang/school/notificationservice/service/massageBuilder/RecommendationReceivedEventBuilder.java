package faang.school.notificationservice.service.massageBuilder;

import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecommendationReceivedEventBuilder implements MessageBuilder<RecommendationReceivedEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        log.debug("Message building process..." + " with event: " + event);
        return messageSource.getMessage(
                "recommendation_received_channel",
                new Object[]{event.getAuthorId()},
                locale);
    }
}
