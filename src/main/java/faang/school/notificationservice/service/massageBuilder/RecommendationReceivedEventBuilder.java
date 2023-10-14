package faang.school.notificationservice.service.massageBuilder;

import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationReceivedEventBuilder implements MessageBuilder<RecommendationReceivedEvent> {
    private final MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        return messageSource.getMessage(
                "recommendation_received_channel",
                new Object[]{event.getAuthorId()},
                locale);
    }
}
