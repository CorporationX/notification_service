package faang.school.notificationservice.builder;

import faang.school.notificationservice.event.RecommendationRequestedEvent;
import org.springframework.stereotype.Component;

@Component
public class RecommendationRequestMessageBuilder
        implements MessageBuilder<RecommendationRequestedEvent>,
        MessageTypeProvider<RecommendationRequestedEvent> {

    @Override
    public String buildMessage(RecommendationRequestedEvent event) {
        return "Пользователь с ID " + event.getSenderId() + " запросил рекомендацию.";
    }

    @Override
    public Class<RecommendationRequestedEvent> getInstance() {
        return RecommendationRequestedEvent.class;
    }
}