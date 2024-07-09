package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.NotificationData;
import faang.school.notificationservice.dto.TariffRateChangeEvent;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class TariffRateChangeListener extends AbstractListener<TariffRateChangeEvent> implements MessageListener {
    public TariffRateChangeListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                    List<MessageBuilder<TariffRateChangeEvent>> messageBuilders,
                                    List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        TariffRateChangeEvent tariffRateChangeEvent = getEvent(message, TariffRateChangeEvent.class);
        NotificationData notificationData = getNotificationData(tariffRateChangeEvent.getSavingsAccountId());
        String textOfMessage = getMessage(tariffRateChangeEvent, Locale.UK, notificationData);
        UserNotificationDto userDto = userServiceClient.getDtoForNotification(tariffRateChangeEvent.getOwnerId());
        sendNotification(userDto, textOfMessage);
        log.info("Sending notification for event {}", tariffRateChangeEvent);
    }
}