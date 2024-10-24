package faang.school.notificationservice.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClientMock;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.dto.CommentNotificationEventDto;
import faang.school.notificationservice.redis.event.CommentNotificationEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommentNotificationEventListener extends AbstractEventListener<CommentNotificationEvent> {
    public CommentNotificationEventListener(UserServiceClientMock userServiceClient,
                                            List<MessageBuilder<?>> messageBuilders,
                                            List<NotificationService> notificationServices,
                                            ObjectMapper objectMapper) {
        super(userServiceClient, messageBuilders, notificationServices, objectMapper, CommentNotificationEvent.class);
    }

    @Override
    protected void processEvent(CommentNotificationEvent event) {
        MessageBuilder<CommentNotificationEventDto> messageBuilder =
                (MessageBuilder<CommentNotificationEventDto>) defineBuilder(CommentNotificationEventDto.class);
        UserDto postOwnerDto = getUserDto(event.getAuthorPostId());
        UserDto commentOwnerDto = getUserDto(event.getAuthorCommentId());

        CommentNotificationEventDto commentNotificationEventDto = CommentNotificationEventDto.builder()
                .commentAuthorName(commentOwnerDto.getUsername())
                .postAuthorName(postOwnerDto.getUsername())
                .content(event.getContent())
                .build();
        String message = messageBuilder.buildMessage(commentNotificationEventDto, postOwnerDto.getLocale());
        sendNotification(postOwnerDto, message);
    }
}