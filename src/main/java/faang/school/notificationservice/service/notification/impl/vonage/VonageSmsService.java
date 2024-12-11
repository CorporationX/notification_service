package faang.school.notificationservice.service.notification.impl.vonage;

import cn.hutool.core.lang.Snowflake;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.resilience4j.Resilience4jProperties;
import faang.school.notificationservice.config.vonage.VonageConfig;
import faang.school.notificationservice.dto.user.PreferredContact;
import faang.school.notificationservice.dto.user.UserForNotificationDto;
import faang.school.notificationservice.model.MessageDeliveryStatus;
import faang.school.notificationservice.model.SmsMessage;
import faang.school.notificationservice.service.jpa.SmsMessageService;
import faang.school.notificationservice.service.notification.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class VonageSmsService implements NotificationService {

    private final VonageConfig vonageConfig;
    private final VonageClient vonageClient;
    private final SmsMessageService smsMessageService;
    private final Snowflake snowflake;

    @Retry(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @CircuitBreaker(name = Resilience4jProperties.DEFAULT_RETRY_CONFIG_NAME)
    @Override
    public void send(UserForNotificationDto receiver, String message) {
        log.info("Start sending sms");
        SmsMessage smsMessage = buildSmsMessage(receiver, message);
        smsMessageService.saveSmsMessageAsync(smsMessage);

        TextMessage textMessage = buildVonageTextMessage(receiver, message, smsMessage);
        vonageClient.getSmsClient().submitMessage(textMessage);
        log.info("SMS message with uid {} was sent", smsMessage.getUid());
    }

    private TextMessage buildVonageTextMessage(UserForNotificationDto receiver, String message, SmsMessage smsMessage) {
        TextMessage textMessage = new TextMessage(
                vonageConfig.getBrandNumber(),
                receiver.phone(),
                message);
        textMessage.setClientReference(smsMessage.getUid().toString());
        textMessage.setCallbackUrl(vonageConfig.getCallbackUrl());
        return textMessage;
    }

    private SmsMessage buildSmsMessage(UserForNotificationDto receiver, String message) {
        return SmsMessage.builder()
                .uid(snowflake.nextId())
                .content(message)
                .receiverId(receiver.id())
                .deliveryStatus(MessageDeliveryStatus.IN_QUEUE)
                .sendTime(LocalDateTime.now())
                .build();
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.PHONE;
    }
}
