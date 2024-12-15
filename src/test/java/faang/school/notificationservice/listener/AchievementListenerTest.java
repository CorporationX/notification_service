package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.AchievementEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AchievementListenerTest {
    @InjectMocks
    private AchievementListener listener;

    @Mock
    private EventListenerHandler<AchievementEvent> achievementEventEventListenerHandler;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private Message message;

    @Test
    public void testOnMessage() {
        listener.onMessage(message, new byte[]{});
        verify(achievementEventEventListenerHandler).eventHandler(any(), any(), any());
    }
}
