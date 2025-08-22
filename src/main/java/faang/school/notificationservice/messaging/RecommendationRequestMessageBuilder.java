package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.RecommendationRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestMessageBuilder implements MessageBuilder<RecommendationRequestEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return RecommendationRequestEvent.class;
    }

    @Override
    public String buildMessage(RecommendationRequestEvent event, Locale locale) {
        long requesterId = event.getRequesterId();
        return messageSource.getMessage("recommendation.request.message",
                new Object[] { requesterId },
                locale);
    }
}