package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component("recommendation-request-channel")
public class RecommendationRequestedEventListener extends AbstractEventListener<RecommendationRequestedEvent> implements MessageListener {
    @Autowired
    public RecommendationRequestedEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<MessageBuilder<?>> messageBuilders,
            List<NotificationService> notificationServices
    ) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    protected Class<RecommendationRequestedEvent> getEventClass() {
        return RecommendationRequestedEvent.class;
    }

    @Override
    protected void handleEvent(RecommendationRequestedEvent event) {
        UserDto receiverDto = userServiceClient.getUser(event.getReceiverId());
        UserDto authorDto = userServiceClient.getUser(event.getRequesterId());

        Object[] placeholders = {receiverDto.getUsername(), authorDto.getUsername(), event.getRequestId()};

        Locale locale = LocaleContextHolder.getLocale();
        String message = getMessage(event, locale, placeholders);

        sendNotification(receiverDto.getId(), message);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationRequestedEvent event = objectMapper.readValue(message.getBody(), RecommendationRequestedEvent.class);
            handleEvent(event);
        } catch (IOException e) {
            log.error("Error deserializing RecommendationRequestedEvent: {}", e.getMessage(), e);
        }
    }
}
