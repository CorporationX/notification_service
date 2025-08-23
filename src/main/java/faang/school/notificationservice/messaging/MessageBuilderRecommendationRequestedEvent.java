package faang.school.notificationservice.messaging;


import faang.school.notificationservice.event.RecommendationRequestedEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageBuilderRecommendationRequestedEvent implements MessageBuilder<RecommendationRequestedEvent> {

    public boolean supports(Class<?> clazz) {
        return RecommendationRequestedEvent.class.equals(clazz);
    }

    @Override
    public String buildMessage(RecommendationRequestedEvent event, Locale locale) {
        return "Пользователь с ID " + event.getSenderId() +
                " запросил у вас рекомендацию. ID запроса: " + event.getRecommendationId();
    }

    @Override
    public Class<RecommendationRequestedEvent> getInstance() {
        return RecommendationRequestedEvent.class;
    }
}