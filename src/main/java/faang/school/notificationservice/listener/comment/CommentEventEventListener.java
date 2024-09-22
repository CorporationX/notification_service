package faang.school.notificationservice.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.comment.CommentEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CommentEventEventListener extends AbstractEventListener<CommentEvent> {

    private final UserServiceClient userServiceClient;

    public CommentEventEventListener(ObjectMapper objectMapper,
                                     List<NotificationService> notificationServices,
                                     CommentEventMessageBuilder commentEventMessageBuilder, UserServiceClient userServiceClient) {
        super(notificationServices, objectMapper, commentEventMessageBuilder, CommentEvent.class);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public List<UserDto> getNotifiedUsers(CommentEvent event) {
        List<UserDto> users = new ArrayList<>();
        UserDto user = userServiceClient.getUser(event.getPostAuthorId());
        users.add(user);
        return users;
    }

    @Override
    public Object[] getArgs(CommentEvent event) {
        return new Object[]{event.getPostAuthorId(), event.getPostId(), event.getAuthorId(), event.getComment()};
    }

}
