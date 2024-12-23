package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.messaging.CommentMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserFeignService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {
    private final CommentMessageBuilder commentMessageBuilder;
    private final ObjectMapper objectMapper;
    private final UserFeignService userFeignService;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(@Nonnull Message message, byte[] pattern) {
        try {
            CommentEvent event = objectMapper.readValue(message.getBody(), CommentEvent.class);

            UserContactsDto user = userFeignService.getUserContacts(event.getPostAuthorId());
            event.setPostAuthorName(user.getUsername());

            notificationServices.stream()
                    .filter(service -> user.getPreference().equals(service.getPreferredContact()))
                    .findFirst()
                    .ifPresentOrElse(
                            service -> service.send(user, commentMessageBuilder.buildMessage(event, LocaleContextHolder.getLocale())),
                            () -> log.error("No notification service found for user {}", user.getId())
                    );
        } catch (IOException e) {
            log.error("Error while serializing like event from redis. Error: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error while sending like event to user. Error: {}", e.getMessage(), e);
        }
    }
}

