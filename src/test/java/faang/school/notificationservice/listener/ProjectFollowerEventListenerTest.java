package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.ProjectFollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectFollowerEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ProjectFollowerMessageBuilder messageBuilder;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private ProjectFollowerEventListener projectFollowerEventListener;

    private ProjectFollowerEvent followerEvent;
    private UserNotificationDto userNotificationDto;
    private Message message;
    private Class tClass;

    @BeforeEach
    void setUp() {
        objectMapper = mock(ObjectMapper.class);
        messageBuilder = mock(ProjectFollowerMessageBuilder.class);
        userServiceClient = mock(UserServiceClient.class);
        notificationService = mock(NotificationService.class);
        projectFollowerEventListener = new ProjectFollowerEventListener(objectMapper, userServiceClient,
                List.of(messageBuilder), List.of(notificationService));
        followerEvent = ProjectFollowerEvent.builder()
                .followerId(1L)
                .ownerId(2L)
                .build();
        userNotificationDto = UserNotificationDto.builder()
                .username("Igor")
                .build();
        tClass = followerEvent.getClass();
        message = mock(Message.class);
    }

    @Test
    public void whenOnMessageSuccessfully() throws IOException {
        byte[] body = new byte[0];
        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, ProjectFollowerEvent.class)).thenReturn(followerEvent);
        when(messageBuilder.getInstance()).thenReturn(tClass);
        when(messageBuilder.buildMessage(any(), any(), any())).thenReturn("some text");
        when(userServiceClient.getDtoForNotification(anyLong())).thenReturn(userNotificationDto);
        projectFollowerEventListener.onMessage(message, body);
        verify(objectMapper).readValue(message.getBody(), ProjectFollowerEvent.class);
    }
}