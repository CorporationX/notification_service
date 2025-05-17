package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RequestEventEvent;
import faang.school.notificationservice.exception.BuildMessageFailedException;
import faang.school.notificationservice.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestEventEventMessageBuilder implements MessageBuilder<RequestEventEvent> {

    private final List<RequestEventEventStatusMessageBuilder> messageBuilders;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return RequestEventEvent.class;
    }

    @Override
    public String buildMessage(RequestEventEvent event, Locale locale) {
        var userDto = getUserById(event.userId());

        for (var messageBuilder : messageBuilders) {
            if (messageBuilder.isApplicable(event)) {
                return messageBuilder.buildMessage(event, userDto, locale);
            }
        }

        log.error("Invalid request event message type: {}", event.requestStatus());

        throw new BuildMessageFailedException("Invalid request event message type: %s".formatted(
                event.requestStatus()));
    }

    private UserDto getUserById(long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (Exception ex) {
            throw new UserNotFoundException("User with id #%d is not found".formatted(userId), ex);
        }
    }
}

