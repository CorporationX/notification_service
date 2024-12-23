package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.RecommendationMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecommendationReceivedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserFeignService userFeignService;
    private final RecommendationMessageBuilder recommendationMessageBuilder;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(@Nonnull Message message, byte[] pattern) {
        try {
            RecommendationReceivedEvent event = objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class);
            handleEvent(event);
        } catch (IOException e) {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            log.error("Error while serializing {} from redis. Error: {}", messageBody, e.getMessage(), e);
        } catch (Exception e) {
            log.error("An error occurred while sending subscription event to user. Error: {}", e.getMessage(), e);
        }
    }

    private void handleEvent(RecommendationReceivedEvent event) {
        UserContactsDto receiverDto = userFeignService.getUserContacts(event.getReceiverId());

        String message = recommendationMessageBuilder.buildMessage(event, LocaleContextHolder.getLocale());

        notificationServices.stream()
                .filter(service -> receiverDto.getPreference().equals(service.getPreferredContact()))
                .findFirst()
                .ifPresentOrElse(
                        service -> {
                            service.send(receiverDto, message);
                            log.info("Message sent to user {} via {}", receiverDto.getId(), receiverDto.getPreference());
                        },
                        () -> log.error("No notification service found for user {}", receiverDto.getId())
                );

    }
}
