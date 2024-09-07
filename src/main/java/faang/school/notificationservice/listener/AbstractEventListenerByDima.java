package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListenerByDima<E> implements MessageListener {
    private final List<NotificationService> notifiers;
    private final ObjectMapper mapper;
    private final MessageBuilder<E> messageBuilder;
    private final Class<E> eventType;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        E event;
        try {
            event = mapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<UserDto> users = getNotifiedUsers(event);
        log.info(users + " notified successfully about event:" + event);

        users.forEach(user -> notifiers.stream()
                .filter(notifier -> canBeNotified(notifier, user))
                .forEach(notifier -> {
                    String msg = messageBuilder.buildMessage(event, Locale.US, getArgs(event));
                    notifier.send(user, msg);
                })
        );
    }

    protected abstract List<UserDto> getNotifiedUsers(E event);
    protected abstract Object[] getArgs(E event);

    protected boolean canBeNotified(NotificationService notification, UserDto user) {
        UserDto.PreferredContact preferred = user.getPreference();

        if (preferred == null) {
            return false;
        }

        return preferred == notification.getPreferredContact();
    }
}
