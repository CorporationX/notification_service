package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestMessageBuilder implements MessageBuilder<RecommendationRequestEvent>{
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(RecommendationRequestEvent eventType, Locale locale) {
        UserDto recommendationRequest = userServiceClient.getUser(eventType.getAuthorId());
        return messageSource.getMessage("recommendationRequest.new", new Object[]{recommendationRequest.getUsername()}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return RecommendationRequestEvent.class;
    }
}
