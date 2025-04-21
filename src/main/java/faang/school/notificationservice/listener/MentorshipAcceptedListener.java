package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class MentorshipAcceptedListener extends AbstractListener<MentorshipAcceptedEventDto> implements MessageListener {

    public MentorshipAcceptedListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                      List<NotificationService> notificationServices,
                                      List<MessageBuilder<MentorshipAcceptedEventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Получено событие mentorship_accepted_topic: {}", new String(message.getBody()));

        handleEvent(message, MentorshipAcceptedEventDto.class, event -> {
            log.info("Обрабатываем MentorshipAcceptedEventDto: {}", event);
            UserDto requester = userServiceClient.getUser(event.getRequesterId());
            log.info("Получен UserDto: {}", requester);
            String text = getMessage(event, Locale.UK);
            log.info("Сформировано сообщение: {}", text);
            sendNotification(requester.getId(), text);
        });
    }
}