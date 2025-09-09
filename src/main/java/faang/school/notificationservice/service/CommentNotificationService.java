package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.exception.UserServiceException;
import faang.school.notificationservice.messaging.MessageBuilder;
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
        log.info("Processing comment notification for post {} by user {}",
                event.postId(), event.commentAuthorId());

        UserDto user = getUserById(event.postAuthorId());
        if (user == null) {
            throw new UserNotFoundException(event.postAuthorId());
        }

        MessageBuilder<CommentEvent> builder = findMessageBuilder(CommentEvent.class);
        String message = builder.buildMessage(event, Locale.ENGLISH);

        NotificationService notificationService = notificationServices.get(user.getPreference());
        if (notificationService == null) {
            log.warn("No notification service found for preferred contact: {}", user.getPreference());
            return;
        }

        try {
            notificationService.send(user, message);
            log.info("Comment notification sent successfully to user {} via {}",
                    user.getId(), user.getPreference());
        } catch (Exception e) {
            throw new UserServiceException("Failed to send notification to user " + user.getId(), e);
        }
    }

    private UserDto getUserById(long userId) {
        try {
            return userServiceClient.getUser(userId);
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundException(userId);
        } catch (FeignException e) {
            throw new UserServiceException("Error calling user service for user " + userId, e);
        } catch (Exception e) {
            throw new UserServiceException("Unexpected error calling user service for user " + userId, e);
        }
    }

    private <T> MessageBuilder<T> findMessageBuilder(Class<T> eventClass) {
        return (MessageBuilder<T>) messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(eventClass))
                .map(eventClass::cast)
                .findFirst()
                .orElseThrow(() -> new MessageBuilderNotFoundException(eventClass.getSimpleName()));
    }
}
