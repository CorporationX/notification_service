package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.config.redis.Channels;
import faang.school.notificationservice.dto.StockAlertDto;
import faang.school.notificationservice.mapper.NotificationMapper;
import faang.school.notificationservice.messaging.StockAlertMessageBuilder;
import faang.school.notificationservice.service.impl.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockAlertListener implements MessageListener, CustomListener {

    private final Channels channels;
    private final StockAlertMessageBuilder stockAlertMessageBuilder;
    private final EmailNotificationService emailNotificationService;
    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            List<StockAlertDto> promoList = objectMapper.readValue(message.getBody(), new TypeReference<>() {});

            promoList.forEach(stockAlert -> {
                String messageText = stockAlertMessageBuilder.buildMessage(stockAlert, Locale.ENGLISH);
                emailNotificationService.send(notificationMapper.toUserDto(stockAlert), messageText);
                log.info("Notification successfully sent to user ID: {}", stockAlert.userId());
            });

        } catch (IOException e) {
            log.error("Error deserializing JSON to object", e);
            throw new RuntimeException("Error deserializing JSON to object", e);
        }
    }

    @Override
    public String getTopicName() {
        return channels.getStockAlertChannel();
    }
}
