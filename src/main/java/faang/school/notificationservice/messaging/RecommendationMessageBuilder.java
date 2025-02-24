package faang.school.notificationservice.messaging;


import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationMessageBuilder implements MessageBuilder<RecommendationRequestedEvent> {
    @Override
    public String buildMessage(RecommendationRequestedEvent event, Locale locale) {
        return messageSource.getMessage("message.recommendation", null, locale);
    }

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return RecommendationRequestedEvent.class;
    }
}
