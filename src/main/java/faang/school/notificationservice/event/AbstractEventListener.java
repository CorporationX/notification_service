package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final Map<Class<?>, MessageBuilder<?>> messageBuilders;
    private static final Logger log = LoggerFactory.getLogger(AbstractEventListener.class);

    public String getMessage(Class<?> eventType, Locale locale, Object... args) {
        MessageBuilder<T> messageBuilder = (MessageBuilder<T>) messageBuilders.get(eventType);
        if (messageBuilder == null) {
            throw new IllegalArgumentException("MessageBuilder не найден для события: " + eventType.getSimpleName());
        }
        return messageBuilder.buildMessage((T) args[0], locale);
    }

    public UserDto getUser(Long userId) {
        return Optional.ofNullable(userServiceClient.getUser(userId))
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + userId));
    }
}