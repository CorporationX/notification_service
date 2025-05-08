package faang.school.notificationservice.service.kafka.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.request.RequestResponseDto;
import faang.school.notificationservice.service.NotificationSender;
import faang.school.notificationservice.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestKafkaListener {
    public static final String RECEIVED_MESSAGE_FROM_KAFKA = "Received message from kafka: {}";
    private final JsonUtils jsonUtils;
    private final NotificationSender notificationSender;
    private final UserServiceClient userServiceClient;

    @Value("${messages.request.block-account}")
    private String requestBlockAccountNotification;

    @Value("${messages.request.close-account}")
    private String requestCloseAccountNotification;

    @Value("${messages.request.create-account}")
    private String requestCreateAccountNotification;

    @Value("${messages.request.transfer-funds}")
    private String requestTransferFundsNotification;

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.request.block-account-topic}",
            groupId = "${spring.kafka.consumer.groups.request-notification-group}"
    )
    public void blockAccountListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        RequestResponseDto requestResponseDto = jsonUtils.deserialize(message, RequestResponseDto.class);
        String notification = requestBlockAccountNotification.formatted(
                getUserDto(requestResponseDto.getUserId()).getUsername(),
                requestResponseDto.getStatus()
        );
        notificationSender.sendNotification(notification, requestResponseDto.getUserId());
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.request.close-account-topic}",
            groupId = "${spring.kafka.consumer.groups.request-notification-group}"
    )
    public void closeAccountListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        RequestResponseDto requestResponseDto = jsonUtils.deserialize(message, RequestResponseDto.class);
        String notification = requestCloseAccountNotification.formatted(
                getUserDto(requestResponseDto.getUserId()).getUsername(),
                requestResponseDto.getStatus()
        );
        notificationSender.sendNotification(notification, requestResponseDto.getUserId());
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.request.create-account-topic}",
            groupId = "${spring.kafka.consumer.groups.request-notification-group}"
    )
    public void createAccountListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        RequestResponseDto requestResponseDto = jsonUtils.deserialize(message, RequestResponseDto.class);
        String notification = requestCreateAccountNotification.formatted(
                getUserDto(requestResponseDto.getUserId()).getUsername(),
                requestResponseDto.getStatus()
        );
        notificationSender.sendNotification(notification, requestResponseDto.getUserId());
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.request.transfer-funds-topic}",
            groupId = "${spring.kafka.consumer.groups.request-notification-group}"
    )
    public void transferFundsListener(String message, Acknowledgment acknowledgment) {
        log.info(RECEIVED_MESSAGE_FROM_KAFKA, message);
        RequestResponseDto requestResponseDto = jsonUtils.deserialize(message, RequestResponseDto.class);
        String notification = requestTransferFundsNotification.formatted(
                getUserDto(requestResponseDto.getUserId()).getUsername(),
                requestResponseDto.getStatus()
        );
        notificationSender.sendNotification(notification, requestResponseDto.getUserId());
    }

    private UserDto getUserDto(Long userId) {
        return userServiceClient.getUser(userId);
    }
}
