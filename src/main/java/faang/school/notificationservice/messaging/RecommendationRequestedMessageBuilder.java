package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Строитель сообщений для запроса на рекомендацию
 *
 * @author Linempy
 * @since 14.08.2025
 */
@Component
@RequiredArgsConstructor
public class RecommendationRequestedMessageBuilder implements MessageBuilder<RecommendationRequestedEvent> {

    private final MessageSource messageSource;
    private final UserServiceClient userClient;

    @Override
    public Class<?> getInstance() {
        return RecommendationRequestedEvent.class;
    }

    @Override
    public String buildMessage(RecommendationRequestedEvent event, Locale locale) {
        UserDto requester = userClient.getUser(event.requesterId());
        UserDto receiver = userClient.getUser(event.receiverId());

        if (requester == null || receiver == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        return messageSource.getMessage(
                "recommendation-request.new",
                new Object[]{receiver.getUsername(), requester.getUsername()},
                locale
        );
    }
}