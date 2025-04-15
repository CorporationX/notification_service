package faang.school.notificationservice.service.kafka.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.PremiumNotificationDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationMethodNotSupportedException;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static faang.school.notificationservice.messages.ErrorMessages.FAILED_TO_ACKNOWLEDGE_KAFKA_MESSAGE;
import static faang.school.notificationservice.messages.ErrorMessages.NOTIFICATION_METHOD_IS_NOT_SUPPORTED;

@Component
@Slf4j
@RequiredArgsConstructor
public class PremiumKafkaListener {
    public static final String RECEIVED_MESSAGE_FROM_KAFKA = "Received message from kafka: {}";
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final JsonUtils jsonUtils;

    @Value("${messages.premium.premium-bought}")
    private String premiumBoughtNotification;

    @Value("${messages.premium.premium-expired}")
    private String premiumExpiredNotification;

    @Value("${messages.premium.premium-expire-soon}")
    private String premiumExpireSoonNotification;

    @Value("${messages.premium.premium-renew-failed}")
    private String premiumRenewFailedNotification;

    @Value("${messages.premium.premium-updated}")
    private String premiumUpdatedNotification;

    @Value("${messages.premium.premium-payment-failed}")
    private String premiumPaymentFailedNotification;

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.premium.bought-topic}",
            groupId = "${spring.kafka.consumer.groups.premium.premium-bought-group}"
    )
    public void premiumBoughtListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        PremiumNotificationDto premiumNotificationDto = jsonUtils.deserialize(message, PremiumNotificationDto.class);
        String notification = premiumBoughtNotification.formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getStartDate().format(formatter),
                premiumNotificationDto.getEndDate().format(formatter)
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
        PremiumNotificationDto premiumNotificationDto = jsonUtils.deserialize(message, PremiumNotificationDto.class);
        String notification = premiumExpiredNotification.formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getEndDate().format(formatter)
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
        PremiumNotificationDto premiumNotificationDto = jsonUtils.deserialize(message, PremiumNotificationDto.class);
        String notification = premiumExpireSoonNotification.formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getEndDate().format(formatter)
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
        PremiumNotificationDto premiumNotificationDto = jsonUtils.deserialize(message, PremiumNotificationDto.class);
        String notification = premiumRenewFailedNotification.formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getEndDate().format(formatter)
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
        PremiumNotificationDto premiumNotificationDto = jsonUtils.deserialize(message, PremiumNotificationDto.class);
        String notification = premiumUpdatedNotification.formatted(
                premiumNotificationDto.getPremiumType().getMonths(),
                premiumNotificationDto.getStartDate().format(formatter),
                premiumNotificationDto.getEndDate().format(formatter)
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
        PremiumNotificationDto premiumNotificationDto = jsonUtils.deserialize(message, PremiumNotificationDto.class);
        String notification = premiumPaymentFailedNotification.formatted(
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
                .orElseThrow(() -> new NotificationMethodNotSupportedException(
                        NOTIFICATION_METHOD_IS_NOT_SUPPORTED.formatted(userDto.getPreference())))
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
}
