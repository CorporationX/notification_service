package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RequestEventEvent;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.messaging.RequestEventEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestEventEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final RequestEventEventMessageBuilder likeEventMessageBuilder;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channelName = "";

        try {
            channelName = new String(message.getChannel());
            var payload = new String(message.getBody());

            var requestEventEvent = objectMapper.readValue(payload, RequestEventEvent.class);
            var messageToSend = likeEventMessageBuilder.buildMessage(requestEventEvent, Locale.getDefault());

            sendNotification(requestEventEvent, messageToSend);
        } catch (RuntimeException ex) {
            log.error("Processing message from channel {} is failed: {}", channelName, ex.getMessage(), ex);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendNotification(RequestEventEvent requestEventEvent, String messageToSend) {
        var userDto = getUser(requestEventEvent.userId());
        var requiredPreference = userDto.getPreference();
        var sendFutures = notificationServices.stream()
                .filter(service -> service.getPreferredContact() == requiredPreference)
                .map(service -> CompletableFuture.runAsync(() -> service.send(userDto, messageToSend)))
                .toList();

        CompletableFuture.allOf(sendFutures.toArray(new CompletableFuture[0]));
    }

    private UserDto getUser(long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (Exception ex) {
            throw new UserNotFoundException("User with id #%d is not found".formatted(userId), ex);
        }
    }
}
