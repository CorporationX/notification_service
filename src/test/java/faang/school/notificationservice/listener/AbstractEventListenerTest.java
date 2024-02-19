package faang.school.notificationservice.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {
    @InjectMocks
    private FollowerEventListener followerEventListener;
    @Mock
    protected UserServiceClient userServiceClient;
    @Mock
    protected List<NotificationService> notificationServiceList;
    @Mock
    protected List<MessageBuilder<FollowerEvent>> messageBuilderList;
    @Mock
    protected ObjectMapper objectMapper;

    @Test
    public void testMessageBuilderExistsIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> followerEventListener.getMessage(new FollowerEvent(), Locale.UK));
    }

    @Test
    public void testPrefferedContactIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> followerEventListener.sendNotification(1L ,"message"));
    }

}
