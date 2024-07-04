package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;


@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final List<NotificationService> notificationServiceList;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final UserServiceClient userServiceClient;
    protected final PostServiceClient postServiceClient;


    protected String getMessage(T event, Locale locale) {
      return messageBuilders.stream().filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
              .findFirst().map(messageBuilder -> messageBuilder.buildMessage(event, locale))
              .orElseThrow(() -> new IllegalArgumentException("No message builder found for the given event type"));
    }


    protected void sendNotification(long postId, String message, String messagesHeader) {
        long postAuthorId = postServiceClient.getUserIdByPostId(postId);
        UserDto userDto = userServiceClient.getUser(postAuthorId);
        notificationServiceList.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreferenceContact()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Prefer contact not found")).send(userDto, message, messagesHeader);
    }
}