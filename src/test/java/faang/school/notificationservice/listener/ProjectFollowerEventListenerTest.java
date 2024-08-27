package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.ProjectFollowerMessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectFollowerEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ProjectFollowerMessageBuilder messageBuilder;

    @InjectMocks
    private ProjectFollowerEventListener eventListener;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setUsername("TestUser");
        userDto.setPreference(UserDto.PreferredContact.SMS);


        eventListener = new ProjectFollowerEventListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(notificationService),
                Collections.singletonList(messageBuilder)
        );
    }

    @Test
    void testOnMessage() throws IOException {
        ProjectFollowerEvent event = new ProjectFollowerEvent();
        event.setFollowerId(1L);
        event.setAuthorId(2L);

        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(messageBuilder.getInstance()).thenReturn((Class) ProjectFollowerEvent.class);
        when(messageBuilder.buildMessage(any(ProjectFollowerEvent.class), any(Locale.class), any(Object[].class)))
                .thenReturn("Test message");
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        when(objectMapper.readValue(any(byte[].class), eq(ProjectFollowerEvent.class))).thenReturn(event);

        byte[] messageBody = new byte[0];
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody);

        eventListener.onMessage(message, null);

        verify(objectMapper).readValue(messageBody, ProjectFollowerEvent.class);
        verify(messageBuilder).buildMessage(eq(event), eq(Locale.ENGLISH), any(Object[].class));
        verify(notificationService).send(any(UserDto.class), eq("Test message"));
    }

    @Test
    void testOnMessage_whenExceptionThrown() throws IOException {
        ProjectFollowerEvent event = new ProjectFollowerEvent();
        event.setFollowerId(1L);
        event.setAuthorId(2L);

        byte[] messageBody = new byte[0];
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody);

        when(objectMapper.readValue(messageBody, ProjectFollowerEvent.class))
                .thenThrow(new IOException("Failed to read message"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            eventListener.onMessage(message, null);
        });

        assertEquals("java.io.IOException: Failed to read message", thrown.getMessage());
    }
}
