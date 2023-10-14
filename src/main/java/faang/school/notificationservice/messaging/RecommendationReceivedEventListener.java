package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class RecommendationReceivedEventListener extends AbstractEventListener<RecommendationReceivedEvent> implements MessageListener {
    public RecommendationReceivedEventListener(UserServiceClient userServiceClient,
                                              List<MessageBuilder<RecommendationReceivedEvent>> messageBuilder,
                                              List<NotificationService> notificationService,
                                              JsonMapper jsonMapper) {
        super(userServiceClient, messageBuilder, notificationService, jsonMapper);
    }
    @Override
    public void onMessage(Message message, byte[] pattern) {
        jsonMapper.toObjectFromByte(message.getBody(), RecommendationReceivedEvent.class)
                .ifPresent(event -> {
                    Locale locale = LocaleContextHolder.getLocale();
                    String msg = getMessage(event, locale);
                    sendNotification(msg, event.getReceiverId());
                });
        log.info(Arrays.toString(message.getBody()) + " " + "send");
    }
}
