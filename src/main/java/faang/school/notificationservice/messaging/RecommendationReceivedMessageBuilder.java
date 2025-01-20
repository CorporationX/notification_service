package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.events.RecommendationReceivedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationReceivedMessageBuilder implements MessageBuilder<RecommendationReceivedEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<RecommendationReceivedEvent> getInstance() {
        return RecommendationReceivedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationReceivedEvent event, Locale locale) {
        return messageSource.getMessage("recommendation.new", new Object[]{event.getAuthorName()}, locale);
    }
}
