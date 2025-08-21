package faang.school.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationRequestEvent;
import faang.school.notificationservice.messaging.RecommendationRequestMessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final UserServiceClient userServiceClient;
    private final TelegramService telegramService;
    private final RecommendationRequestMessageBuilder recommendationRequestMessageBuilder;

    @KafkaListener(topics = "recommendation-request-topic", groupId = "notification-group")
    public void handleRecommendationRequest(String payload) {
        log.info("Получено новое событие (запрос рекомендации): {}", payload);

        RecommendationRequestEvent event = new RecommendationRequestEvent();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            event = objectMapper.readValue(payload, RecommendationRequestEvent.class);
        } catch (Exception e) {
            log.error("Ошибка при десериализации события", e);
        }

        long receiverId = event.getReceiverId();
        UserDto receiver = userServiceClient.getUser(receiverId);

        // В данном примере предполагается,
        // что пользователь (получивший запрос) выбрал в качестве
        // предпочитаемого способа получать уведомления через Telegram
        receiver.setPreference(UserDto.PreferredContact.TELEGRAM);
        // В данном примере предполагается,
        // что Telegram ID пользователя (получившего запрос) 442136473.
        // В реальном приложении, в зависимости от бизнес-логики,
        // Telegram ID пользователя можно получить из базы данных.
        receiver.setId(442136473L);

        String message = recommendationRequestMessageBuilder.buildMessage(
                event,
                receiver.getLocale());

        if (receiver.getPreference() == UserDto.PreferredContact.TELEGRAM) {
            telegramService.send(receiver, message);
        }
    }
}