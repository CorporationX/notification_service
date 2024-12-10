package faang.school.notificationservice.service.notification.impl.vonage;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.redis.RedisConfig;
import faang.school.notificationservice.dto.UserForNotificationDto;
import faang.school.notificationservice.dto.vonage.DeliveryReceipts;
import faang.school.notificationservice.dto.vonage.ErrorCode;
import faang.school.notificationservice.message.producer.MessagePublisher;
import faang.school.notificationservice.model.MessageDeliveryStatus;
import faang.school.notificationservice.model.SmsMessage;
import faang.school.notificationservice.service.jpa.SmsMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryReceiptService {

    private final SmsMessageService smsMessageService;
    private final UserServiceClient userServiceClient;
    private final VonageSmsService vonageSmsService;
    private final MessagePublisher messagePublisher;
    private final RedisConfig redisConfig;

    @Autowired
    public DeliveryReceiptService(SmsMessageService smsMessageService,
                                  UserServiceClient userServiceClient,
                                  VonageSmsService vonageSmsService,
                                  RedisConfig redisConfig,
                                  @Qualifier(RedisConfig.REDIS_PUBLISHER_NAME) MessagePublisher messagePublisher
    ) {
        this.smsMessageService = smsMessageService;
        this.userServiceClient = userServiceClient;
        this.vonageSmsService = vonageSmsService;
        this.messagePublisher = messagePublisher;
        this.redisConfig = redisConfig;
    }

    public void processDeliveryReceipt(DeliveryReceipts deliveryReceipt) {
        log.info("Start process delivery receipt");
        SmsMessage smsMessage = smsMessageService.getSmsMessageByUid(deliveryReceipt.getClientRef());
        smsMessage.setCost(deliveryReceipt.getPrice());

        if (ErrorCode.DELIVERED == deliveryReceipt.getErrorCode()) {

            smsMessage.setDeliveryStatus(MessageDeliveryStatus.DELIVERED);
            log.info("Message with uid {} was delivered", deliveryReceipt.getClientRef());

        } else if (ErrorCode.isRetryable(deliveryReceipt.getErrorCode())) {

            smsMessage.setDeliveryStatus(MessageDeliveryStatus.REPROCESSING);
            UserForNotificationDto messageReceiver =
                    userServiceClient.getUserForNotificationById(smsMessage.getReceiverId());
            vonageSmsService.send(messageReceiver, smsMessage.getContent());
            log.info("Try to send a message with uid {} again ", deliveryReceipt.getMessageId());

        } else {
            smsMessage.setDeliveryStatus(MessageDeliveryStatus.DELIVERY_FAILED);
            messagePublisher.publish(redisConfig.getFailedSmsMessageTopicName(), smsMessage);
            log.info("Message with uid {} was not delivered. Error code {}", deliveryReceipt.getMessageId(),
                    deliveryReceipt.getErrorCode());
        }
        smsMessageService.saveSmsMessageAsync(smsMessage);
    }
}
