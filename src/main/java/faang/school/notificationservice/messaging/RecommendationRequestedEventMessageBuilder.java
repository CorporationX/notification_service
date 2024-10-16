package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.RecommendationRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationRequestedEventMessageBuilder implements MessageBuilder<RecommendationRequestedEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(RecommendationRequestedEvent event, Locale locale) {
        UserDto requester = userServiceClient.getUser(event.requesterId());
        return messageSource.getMessage("recommendation-request.new",
                new Object[]{requester.getUsername()}, locale);
    }
}
