package faang.school.notificationservice.messaging.builder;

import faang.school.notificationservice.dto.RecommendationReceiveEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class RecommendationReceiveMessageBuilder implements MessageBuilder<RecommendationReceiveEvent> {

    private MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationReceiveEvent event, Locale locale) {
        return messageSource.getMessage("recommendation.receive.new", new Object[]{event.receiverId()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return RecommendationReceiveEvent.class;
    }
}
