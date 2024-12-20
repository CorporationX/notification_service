package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.publisher_events.RecommendationReceivedEvent;
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
        return messageSource.getMessage("recommendation.new", new Object[]{event.getAuthorId()}, locale);
    }
}
