package faang.school.notificationservice.messaging.recommendationReceived;

import faang.school.notificationservice.event.recommendationReceived.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class RecommendationReceivedMessageBuilder implements MessageBuilder<RecommendationReceivedEvent> {
    private MessageSource messageSource;
    @Override
    public Class<?> getInstance() {
        return RecommendationReceivedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        return messageSource.getMessage("recommendationReceived.new", new Object[]{event.getAuthorId()}, locale);
    }
}