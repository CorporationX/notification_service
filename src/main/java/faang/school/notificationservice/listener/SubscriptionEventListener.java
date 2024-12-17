package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.SubscriptionEvent;
import faang.school.notificationservice.messaging.SubscriptionMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
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
public class SubscriptionEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserFeignService userFeignService;
    private final SubscriptionMessageBuilder subscriptionMessageBuilder;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            SubscriptionEvent event = objectMapper.readValue(message.getBody(), SubscriptionEvent.class);
            handleEvent(event);
        } catch (IOException e) {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            log.error("An error occurred while serializing {} from redis. Error: {}", messageBody, e.getMessage(), e);
        } catch (Exception e) {
            log.error("An error occurred while sending subscription event to user. Error: {}", e.getMessage(), e);
        }
    }

    private void handleEvent(SubscriptionEvent event) {
        UserContactsDto receiverDto = userFeignService.getUserContacts(event.getFolloweeId());

        String message = subscriptionMessageBuilder.buildMessage(event, LocaleContextHolder.getLocale());

        notificationServices.stream()
                .filter(service -> receiverDto.getPreference().equals(service.getPreferredContact()))
                .findFirst()
                .ifPresentOrElse(service -> {
                            service.send(receiverDto, message);
                            log.info("Message sent to user {} via {}", receiverDto.getId(), receiverDto.getPreference());
                        },
                        () -> log.error("No notification service found for user {}", receiverDto.getId())
                );
    }
}
