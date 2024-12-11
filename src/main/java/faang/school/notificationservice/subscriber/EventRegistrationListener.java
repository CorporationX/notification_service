package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.EventRegistrationNotificationDto;
import faang.school.notificationservice.notification.telegram.TelegramMessageSender;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Data
@RequiredArgsConstructor
public class EventRegistrationListener implements MessageListener {
    public static List<String> messageList = new ArrayList<>();
    private final TelegramMessageSender messageSender;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Async("fixedThreadPool")
    @Override
    public void onMessage(Message message, byte[] pattern) {
        messageList.add(message.toString());
        log.info("Message was added to the queue list: {}", message);

        String jsonMessage = new String(message.getBody());
        EventRegistrationNotificationDto notification;

        try {
            notification = objectMapper
                    .readValue(jsonMessage, EventRegistrationNotificationDto.class);
        } catch (JsonProcessingException e) {
            log.error("Message could not be mapped to dto", e);
            throw new IllegalStateException(e);
        }

        String chatId = notification.getTelegramId();
        String notificationMessage = notification.getMessage();

        try {
            messageSender.sendMessage(chatId, notificationMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
