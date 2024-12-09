package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.MentorshipAcceptedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MentorshipAcceptedEventListener implements MessageListener {
    private final EventListenerHandler<MentorshipAcceptedEvent> mentorshipAcceptedEventListener;
    private final UserServiceClient userServiceClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        mentorshipAcceptedEventListener.eventHandler(
                message,
                MentorshipAcceptedEvent.class,
                event -> userServiceClient.getUser(event.getRequesterUserId())
        );
    }
}
