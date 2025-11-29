package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.RecommendationEventBuilder;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationReceivedMessageBuilder implements MessageBuilder<RecommendationEventBuilder>{
    private final MessageSource messageSource;
    @Override
    public Class<?> getInstance() {
        return RecommendationReceivedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationEventBuilder event, Locale locale) {
        return messageSource.getMessage("recommendation.receive",
                                            new Object[]{event.getAuthor().getUsername(),
                                                        event.getRecommendation().getText()},
                                            locale);
    }
}
