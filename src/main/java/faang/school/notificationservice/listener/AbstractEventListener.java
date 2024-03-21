package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<Event> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<NotificationService> services;
    private final MessageBuilder<Event> messageBuilder;
    private final UserServiceClient userServiceClient;

    public void notify(Message message, Class<Event> type) {
        try {
            Event event = objectMapper.readValue(message.getBody(), type);
            buildAndSendMessage(event);
        } catch (IOException e) {
            log.error("IOException trying read value from json to event {}", type);
            throw new RuntimeException(e);
        }
    }

    protected void buildAndSendMessage(Event event) {
        UserDto receiverUserDto = userServiceClient.getUserUtility(messageBuilder.getReceiverId(event));
        log.debug("Receiver found with user id = {}", receiverUserDto.getId());

        String textMessage = messageBuilder.buildMessage(event, receiverUserDto.getLocale());
        log.debug("Message built successfully {}", textMessage);

        sendMessage(receiverUserDto, textMessage);
    }

    protected void sendMessage(UserDto receiverUserDto, String textMessage) {
        UserDto.PreferredContact contactType = receiverUserDto.getPreference();
        services.stream().filter(service -> service.getPreferredContact() == contactType)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("Notification service with contact type %s not found",
                                contactType)))
                .send(receiverUserDto, textMessage);
        log.info("Notification type = {} successful sent to user with id = {}", contactType, receiverUserDto.getId());
    }
}