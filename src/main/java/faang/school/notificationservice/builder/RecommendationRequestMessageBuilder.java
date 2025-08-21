package faang.school.notificationservice.builder;

import faang.school.notificationservice.event.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class RecommendationRequestMessageBuilder implements MessageBuilder<RecommendationRequestedEvent> {

    @Override
    public String buildMessage(RecommendationRequestedEvent event, Locale locale) {
        return "Пользователь с ID " + event.getAuthorId() + " запросил рекомендацию.";
    }

    @Override
    public Class<RecommendationRequestedEvent> getInstance() {
        return RecommendationRequestedEvent.class;
    }
}