package faang.school.notificationservice.service.massageBuilder;

import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RecommendationReceivedEventBuilder implements MessageBuilder<RecommendationReceivedEvent> {
    private MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        return messageSource.getMessage(
                "recommendation_received_channel",
                new Object[]{event.getAuthorId()},
                locale);
    }
}
