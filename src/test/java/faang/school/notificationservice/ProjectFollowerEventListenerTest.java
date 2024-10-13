package faang.school.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.ProjectFollowerEvent;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.listener.ProjectFollowerEventListener;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.impl.ProjectFollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectFollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private Message message;

    private ProjectFollowerEventListener listener;

    @BeforeEach
    public void setUp() {
        List<NotificationService> notificationServices = Collections.singletonList(notificationService);
        List<MessageBuilder<ProjectFollowerEvent>> messageBuilders = Collections.singletonList(
                new ProjectFollowerMessageBuilder(userServiceClient, messageSource));

        listener = new ProjectFollowerEventListener(objectMapper, userServiceClient,
                notificationServices, messageBuilders);
    }

    @Test
    @DisplayName("Should successfully process ProjectFollowerEvent and send notification")
    public void testOnMessage_Success() throws Exception {
        ProjectFollowerEvent event = new ProjectFollowerEvent();
        event.setFollowerId(1L);
        event.setProjectId(2L);
        event.setCreatorId(3L);

        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, ProjectFollowerEvent.class)).thenReturn(event);

        UserDto creatorDto = new UserDto();
        creatorDto.setId(1L);
        creatorDto.setUsername("Creator");

        when(userServiceClient.getUser(3L)).thenReturn(creatorDto);
        when(userServiceClient.getUser(1L)).thenReturn(new UserDto());
        when(messageSource.getMessage(eq("new.project.follower"), any(), eq(Locale.UK))).thenReturn("Notification message");

        listener.onMessage(message, null);

        verify(notificationService, times(1)).send(eq(creatorDto), eq("Notification message"));
    }

    @Test
    @DisplayName("Should throw EventProcessingException when message parsing fails")
    public void testOnMessage_EventProcessingException() throws Exception {
        byte[] messageBody = new byte[0];
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, ProjectFollowerEvent.class))
                .thenThrow(new IOException("Error parsing"));

        Executable executable = () -> listener.onMessage(message, new byte[0]);

        EventProcessingException exception = assertThrows(EventProcessingException.class, executable);
        assertEquals("Failed to process event of type ProjectFollowerEvent", exception.getMessage());
    }
}