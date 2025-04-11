package faang.school.notificationservice.service.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.PremiumNotificationDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationMethodNotSupportedException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static faang.school.notificationservice.message.ErrorMessages.FAILED_TO_ACKNOWLEDGE_KAFKA_MESSAGE;

@Component
@Slf4j
@RequiredArgsConstructor
public class PremiumKafkaListener {
    public static final String RECEIVED_MESSAGE_FROM_KAFKA = "Received message from kafka: {}";
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.premium.bought-topic}",
            groupId = "${spring.kafka.consumer.groups.premium.premium-bought-group}"
    )
    public void premiumBoughtListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        PremiumNotificationDto premiumNotificationDto = getPremiumNotificationDto(message);
        String notification = "You’ve successfully subscribed to Premium for %s months. Active from %s to %s.".formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getStartDate().toLocalDate(),
                premiumNotificationDto.getEndDate().toLocalDate()
        );
        sendNotification(notification, premiumNotificationDto.getUserId());
        acknowledgeMessage(acknowledgment);
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.premium.expired-topic}",
            groupId = "${spring.kafka.consumer.groups.premium.expired-group}"
    )
    public void premiumExpiredListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        PremiumNotificationDto premiumNotificationDto = getPremiumNotificationDto(message);
        String notification = "Your Premium subscription for %s months expired on %s.".formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getEndDate().toLocalDate()
        );
        sendNotification(notification, premiumNotificationDto.getUserId());
        acknowledgeMessage(acknowledgment);
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.premium.expire-soon-topic}",
            groupId = "${spring.kafka.consumer.groups.premium.expire-soon-group}"
    )
    public void premiumExpireSoonListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        PremiumNotificationDto premiumNotificationDto = getPremiumNotificationDto(message);
        String notification = "Your Premium subscription for %s months will expire soon — on %s.".formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getEndDate().toLocalDate()
        );
        sendNotification(notification, premiumNotificationDto.getUserId());
        acknowledgeMessage(acknowledgment);
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.premium.auto-renew-failed-topic}",
            groupId = "${spring.kafka.consumer.groups.premium.auto-renew-failed-group}"
    )
    public void premiumAutoRenewFailedListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        PremiumNotificationDto premiumNotificationDto = getPremiumNotificationDto(message);
        String notification = "We couldn’t auto-renew your Premium for %s months. Subscription ended on %s.".formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getEndDate().toLocalDate()
        );
        sendNotification(notification, premiumNotificationDto.getUserId());
        acknowledgeMessage(acknowledgment);
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.premium.updated-topic}",
            groupId = "${spring.kafka.consumer.groups.premium.updated-group}"
    )
    public void premiumUpdatedListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        PremiumNotificationDto premiumNotificationDto = getPremiumNotificationDto(message);
        String notification = "Your Premium subscription for %s months has been updated. New period: %s to %s.".formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getStartDate().toLocalDate(),
                premiumNotificationDto.getEndDate().toLocalDate()
        );
        sendNotification(notification, premiumNotificationDto.getUserId());
        acknowledgeMessage(acknowledgment);
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.premium.payment-failed-topic}",
            groupId = "${spring.kafka.consumer.groups.premium.payment-failed-group}"
    )
    public void premiumPaymentFailedListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        PremiumNotificationDto premiumNotificationDto = getPremiumNotificationDto(message);
        String notification = "We couldn’t process your payment for Premium for %s months. Please try again.".formatted(
                premiumNotificationDto.getPremiumType().getMonths()
        );
        sendNotification(notification, premiumNotificationDto.getUserId());
        acknowledgeMessage(acknowledgment);
    }

    private void sendNotification(String message, Long userId) {
        UserDto userDto = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() -> new NotificationMethodNotSupportedException(("Notification method '%s'" +
                        " is not supported").formatted(userDto.getPreference())))
                .send(userDto, message);
    }

    private void acknowledgeMessage(Acknowledgment acknowledgment) {
        try {
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error(FAILED_TO_ACKNOWLEDGE_KAFKA_MESSAGE, e);
            throw new RuntimeException(e);
        }
    }

    private PremiumNotificationDto getPremiumNotificationDto(String json) {
        PremiumNotificationDto premiumNotificationDto;
        try {
            premiumNotificationDto = objectMapper.readValue(json, PremiumNotificationDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error while deserializing PremiumNotificationDto", e);
            throw new RuntimeException(e);
        }
        return premiumNotificationDto;
    }
}
