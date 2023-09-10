package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.EventRecommendationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class RecommendationRequestMessageBuilder implements MessageBuilder<EventRecommendationRequestDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(EventRecommendationRequestDto event, Locale locale) {
        return messageSource.getMessage("recommendation_request.new", new Object[]{event.getRecommendationId()}, locale);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == RecommendationRequestMessageBuilder.class;
    }
}
