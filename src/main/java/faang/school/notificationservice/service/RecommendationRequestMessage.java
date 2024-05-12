package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestMessage implements MessageBuilder<RecommendationRequestEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(RecommendationRequestEvent eventType, Locale locale) {
        UserDto user = userServiceClient.getUser(eventType.getAuthorId());
        String username = user.getUsername();
        return messageSource.getMessage("recommendationRequest.new", new Object[]{username}, locale);
    }

    @Override
    public long getRequestAuthor(RecommendationRequestEvent event) {
        return event.getAuthorId();
    }

    @Override
    public Class<RecommendationRequestEvent> supportEventType() {
        return RecommendationRequestEvent.class;
    }
}
