package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.LikeMessageBuilder;
import faang.school.notificationservice.service.NotificationServiceSelector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LikeEventListener {
    private final UserServiceClient userServiceClient;
    private final LikeMessageBuilder likeMessageBuilder;
    private final NotificationServiceSelector notificationServiceSelector;

    public void handleMessage(LikePostEvent event) {
        long postAuthorId = event.getPostAuthorId();
        UserDto postAuthor = userServiceClient.getUser(postAuthorId);

        String message = likeMessageBuilder.buildMessage(event, Locale.ENGLISH);
        notificationServiceSelector.notifyUser(postAuthor, message);
    }
}
