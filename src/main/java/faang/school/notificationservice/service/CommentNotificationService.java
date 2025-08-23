package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.events.CommentEvent;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentNotificationService {

    private final List<MessageBuilder<?>> messageBuilders;
    private final UserServiceClient userServiceClient;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServices;

    public void sendCommentNotification(CommentEvent event) {
        try {
            log.info("Processing comment notification for post {} by user {}",
                    event.postId(), event.commentAuthorId());

            UserDto user = getUserById(event.postAuthorId());
            if (user == null) {
                log.warn("User not found for id: {}", event.postAuthorId());
                return;
            }

            MessageBuilder<CommentEvent> builder = findMessageBuilder(CommentEvent.class);
            if (builder == null) {
                log.error("No MessageBuilder found for {}", CommentEvent.class.getSimpleName());
                return;
            }

            String message = builder.buildMessage(event, Locale.ENGLISH);

            NotificationService notificationService = notificationServices.get(user.getPreference());
            if (notificationService != null) {
                notificationService.send(user, message);
                log.info("Comment notification sent successfully to user {} via {}",
                        user.getId(), user.getPreference());
            } else {
                log.warn("No notification service found for preferred contact: {}", user.getPreference());
            }

        } catch (Exception e) {
            log.error("Failed to send comment notification for event: {}", event, e);
        }
    }

    private UserDto getUserById(long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (FeignException.NotFound e) {
            log.warn("User not found: {}", userId);
            return null;
        } catch (FeignException e) {
            log.error("Error calling user service for user {}: {} - {}",
                    userId, e.status(), e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Unexpected error calling user service for user {}", userId, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> MessageBuilder<T> findMessageBuilder(Class<T> eventClass) {
        return (MessageBuilder<T>) messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(eventClass))
                .findFirst()
                .orElse(null);
    }
}
