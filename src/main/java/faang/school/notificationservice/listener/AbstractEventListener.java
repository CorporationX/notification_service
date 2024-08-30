package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServiceList;
    private final List<MessageBuilder<T>> messageBuilderList;
    private final Class<T> eventType;
    private final MessageBuilder<T> messageBuilder;

    protected String getMessage(T event, Locale userLocale, Object[] args) {
        return messageBuilderList.stream()
                .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("MessageBuilder with " + event.getClass() + " type not found"))
                .buildMessage(event, userLocale, args);
    }

    protected void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        notificationServiceList.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("NotificationService with preferred user contact " + user.getPreference() + " not found"))
                .send(user, message);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event;
        try {
            event = objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<UserDto> users = getNotifiedUsers(event);

        users.forEach(user -> notificationServiceList.stream()
                .filter(notifier -> canBeNotified(notifier, user))
                .forEach(notifier -> {
                    String msg = messageBuilder.buildMessage(event, Locale.US, getArgs(event));
                    notifier.send(user, msg);
                })
        );
    }

    protected abstract List<UserDto> getNotifiedUsers(T event);
    protected abstract Object[] getArgs(T event);

    protected boolean canBeNotified(NotificationService notification, UserDto user) {
        UserDto.PreferredContact preferred = user.getPreference();

        if (preferred == null) {
            return false;
        }

        return preferred == notification.getPreferredContact();
    }
}
