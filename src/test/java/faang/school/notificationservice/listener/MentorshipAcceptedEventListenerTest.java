package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.MentorshipAcceptedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MentorshipAcceptedEventListenerTest {

    @InjectMocks
    private MentorshipAcceptedEventListener listener;

    @Mock
    private EventListenerHandler<MentorshipAcceptedEvent> mentorshipAcceptedEventListener;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private Message message;

    @Test
    public void testOnMessage() {
        listener.onMessage(message, new byte[]{});
        verify(mentorshipAcceptedEventListener).eventHandler(any(), any(), any());
    }
}
