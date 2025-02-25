package faang.school.notificationservice.service.comment;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.CommentEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentEventService commentEventService;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<CommentEvent> messageBuilder;

    @Override
    public void sendNotification(CommentEvent commentEvent) {
        UserDto receiverUser = userServiceClient.getUser(commentEvent.postAuthorId());
        String messageToSend = messageBuilder.buildMessage(commentEvent, Locale.getDefault());
        commentEventService.apply(receiverUser, messageToSend);
    }
}
