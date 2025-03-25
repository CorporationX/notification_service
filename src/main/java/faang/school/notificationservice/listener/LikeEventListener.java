package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeEventListener implements MessageListener {
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder> messageBuilders;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikePostEvent likePostEvent = objectMapper.readValue(message.getBody(), LikePostEvent.class);
            log.info("Created LikePostEvent object: {}", likePostEvent.toString());
            UserNotificationDto userNotificationDto = userServiceClient.getNotificationUser(likePostEvent.getPostAuthorId());
            String messageToSend = "Like is set to your post by " + userNotificationDto.getUsername();

            notificationServices.stream()
                    .filter(ns -> ns.getPreferredContact() == userNotificationDto.getPreferredContact())
                    .findFirst()
                    .ifPresent(ns -> ns.send(userNotificationDto, messageToSend));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
