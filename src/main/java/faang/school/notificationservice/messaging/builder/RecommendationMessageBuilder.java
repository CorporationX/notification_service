package faang.school.notificationservice.messaging.builder;

import faang.school.notificationservice.dto.RecommendationEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class RecommendationMessageBuilder implements MessageBuilder<RecommendationEvent> {

    private MessageSource messageSource;

    @Override
    public String buildMessage(RecommendationEvent event, Locale locale) {
        return messageSource.getMessage("recommendation.receive", new Object[]{event.receiverId()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return RecommendationEvent.class;
    }
}
