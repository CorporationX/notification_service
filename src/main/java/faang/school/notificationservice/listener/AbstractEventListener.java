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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<E> implements MessageListener {

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

        users.forEach(user -> notifiers.stream()
                .filter(notifier -> canBeNotified(notifier, user))
                .forEach(notifier -> {
                    String msg = messageBuilder.buildMessage(event, Locale.US);
                    notifier.send(user, msg);
                })
        );
    }

    public List<UserDto> getNotifiedUsers(E event) {
        return Collections.emptyList();
    }

    public boolean canBeNotified(NotificationService notification, UserDto user) {
        UserDto.PreferredContact preferred = user.getPreference();

        if (preferred == null) {
            return false;
        }

        return preferred == notification.getPreferredContact();
    }
}

// import faang.school.notificationservice.client.UserServiceClient;
// import faang.school.notificationservice.dto.UserDto;
// import faang.school.notificationservice.messaging.MessageBuilder;
// import faang.school.notificationservice.service.NotificationService;
// import lombok.RequiredArgsConstructor;

// import java.util.List;
// import java.util.Locale;


// @RequiredArgsConstructor
// public abstract class AbstractEventListener<T> {
//     protected final ObjectMapper objectMapper;
//     protected final UserServiceClient userServiceClient;
//     private final List<NotificationService> notificationServiceList;
//     private final List<MessageBuilder<T>> messageBuilderList;

//     protected String getMessage(T event, Locale userLocale, Object[] args) {
//         return messageBuilderList.stream()
//                 .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
//                 .findFirst()
//                 .orElseThrow(() -> new IllegalArgumentException("MessageBuilder with " + event.getClass() + " type not found"))
//                 .buildMessage(event, userLocale, args);
//     }

//     protected void sendNotification(long userId, String message) {
//         UserDto user = userServiceClient.getUser(userId);
//         notificationServiceList.stream()
//                 .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
//                 .findFirst()
//                 .orElseThrow(() -> new IllegalArgumentException("NotificationService with preferred user contact " + user.getPreference() + " not found"))
//                 .send(user, message);

//     }
// }
