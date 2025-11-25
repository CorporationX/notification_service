package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.dto.RecommendationReceivedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationReceivedEventMessageBuilder implements MessageBuilder<RecommendationReceivedEventDto> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return RecommendationReceivedEventDto.class;
    }

    @Override
    public String buildMessage(RecommendationReceivedEventDto event, Locale locale) {
        return messageSource.getMessage("recommendationReceived.new", null, locale);
    }
}
