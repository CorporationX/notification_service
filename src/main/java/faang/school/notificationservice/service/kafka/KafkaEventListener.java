package faang.school.notificationservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.NotificationDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error_message.ErrorMessage;
import faang.school.notificationservice.service.telegram.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaEventListener {
    private final TelegramService telegramService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // добавляем

    public KafkaEventListener(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            NotificationDto dto = objectMapper.readValue(record.value(), NotificationDto.class);
            UserDto user = dto.getUser();

            switch (user.getPreference()) {
                case TELEGRAM -> telegramService.sendMessage(Long.toString(user.getId()), dto.getMessage());
                default -> log.warn("Unknown notification channel: {}", user.getPreference());
            }

        } catch (JsonProcessingException e) {
            log.error(ErrorMessage.ERROR_NOTIFICATION, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}