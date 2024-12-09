package faang.school.notificationservice.service.notification.impl.vonage;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.redis.RedisConfig;
import faang.school.notificationservice.config.resilience4j.Resilience4jProperties;
import faang.school.notificationservice.dto.ErrorCode;
import faang.school.notificationservice.dto.UserForNotificationDto;
import faang.school.notificationservice.dto.VonageDeliveryReceiptsDto;
import faang.school.notificationservice.message.producer.MessagePublisher;
import faang.school.notificationservice.model.MessageDeliveryStatus;
import faang.school.notificationservice.model.SmsMessage;
import faang.school.notificationservice.service.jpa.SmsMessageService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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

    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    public void processDeliveryReceipt(VonageDeliveryReceiptsDto deliveryReceiptDto) {
        log.info("Start process delivery receipt");
        SmsMessage smsMessage = smsMessageService.getSmsMessageByUid(Long.valueOf(deliveryReceiptDto.getMessageId()));
        smsMessage.setCost(deliveryReceiptDto.getPrice());

        if (ErrorCode.DELIVERED.isSame(deliveryReceiptDto.getErrCode())) {

            smsMessage.setDeliveryStatus(MessageDeliveryStatus.DELIVERED);
            log.info("Message with uid {} was delivered", deliveryReceiptDto.getMessageId());

        } else if (ErrorCode.isRetryable(deliveryReceiptDto.getErrCode())) {

            smsMessage.setDeliveryStatus(MessageDeliveryStatus.REPROCESSING);
            UserForNotificationDto messageReceiver =
                    userServiceClient.getUserForNotificationById(smsMessage.getReceiverId());
            vonageSmsService.send(messageReceiver, smsMessage.getContent());
            log.info("Try to send a message with uid {} again ", deliveryReceiptDto.getMessageId());

        } else {
            smsMessage.setDeliveryStatus(MessageDeliveryStatus.DELIVERY_FAILED);
            messagePublisher.publish(redisConfig.getFailedSmsMessageTopicName(), smsMessage);
            log.info("Message with uid {} was not delivered. Error code {}", deliveryReceiptDto.getMessageId(),
                    deliveryReceiptDto.getErrCode());
        }

        smsMessageService.saveSmsMessageAsync(smsMessage);
    }
}
