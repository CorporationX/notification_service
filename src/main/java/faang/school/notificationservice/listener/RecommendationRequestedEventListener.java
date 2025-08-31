package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationRequestedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final MessageBuilder<RecommendationRequestedEvent> messageBuilder;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationRequestedEvent event = objectMapper.readValue(
                    message.getBody(), RecommendationRequestedEvent.class
            );

            String content = messageBuilder.buildMessage(event, Locale.getDefault());

            UserDto receiver = new UserDto();
            receiver.setId(event.getReceiverId());

            notificationService.send(receiver, content);

        } catch (Exception e) {
            log.error("Failed to process RecommendationRequestedEvent message", e);
        }
    }
}