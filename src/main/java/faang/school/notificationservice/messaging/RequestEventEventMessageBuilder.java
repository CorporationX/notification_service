package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RequestEventEvent;
import faang.school.notificationservice.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestEventEventMessageBuilder implements MessageBuilder<RequestEventEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return RequestEventEvent.class;
    }

    @Override
    public String buildMessage(RequestEventEvent event, Locale locale) {
        var userDto = getUserById(event.userId());

        switch (event.requestStatus()) {
            case READY -> {
                return messageSource.getMessage("request.ready",
                        new Object[]{userDto.getUsername(), event.id(), event.timestamp()},
                        Locale.getDefault());
            }
            case TODO -> {
                return messageSource.getMessage("request.created",
                        new Object[]{userDto.getUsername(), event.id(), event.timestamp()},
                        Locale.getDefault());
            }
            case DONE -> {
                return messageSource.getMessage("request.done",
                        new Object[]{userDto.getUsername(), event.id(), event.timestamp()},
                        Locale.getDefault());
            }
            case CANCELLED -> {
                return messageSource.getMessage("request.cancelled",
                        new Object[]{userDto.getUsername(), event.id(), event.timestamp()},
                        Locale.getDefault());
            }
        }

        log.error("Invalid request event message type: {}", event.requestStatus());

        return "";
    }

    private UserDto getUserById(long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (Exception ex) {
            throw new UserNotFoundException("User with id #%d is not found".formatted(userId), ex);
        }
    }
}

