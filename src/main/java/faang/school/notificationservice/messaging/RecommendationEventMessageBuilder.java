package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RecommendationEventMessageBuilder implements MessageBuilder<RecommendationRequestedEventDto> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    @Override
    public String buildMessage(RecommendationRequestedEventDto event, Locale locale) {
        UserDto author = userServiceClient.getUser(event.getAuthorId());
        Object[] args = {author.getUsername(), event.getCreatedAt()};
        return messageSource.getMessage("recommendation.new", args, locale);
    }
}
