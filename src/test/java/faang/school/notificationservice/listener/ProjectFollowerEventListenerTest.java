package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProjectFollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.project.follower.ProjectFollowerMessageBuilder;
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
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private ProjectFollowerEvent event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setUsername("TestUser");
        userDto.setPreference(UserDto.PreferredContact.SMS);

        event = new ProjectFollowerEvent();
        event.setProjectId(1L);
        event.setFollowerId(1L);
        event.setAuthorId(1L);


        eventListener = new ProjectFollowerEventListener(
                objectMapper,
                userServiceClient,
                Collections.singletonList(notificationService),
                Collections.singletonList(messageBuilder),
                messageBuilder
        );
    }

    @Test
    void testGetNotifiedUsers() {
        when(userServiceClient.getUser(event.getFollowerId())).thenReturn(userDto);

        List<UserDto> result = eventListener.getNotifiedUsers(event);

        assertEquals(List.of(userDto), result);
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
