package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.ProfileViewEvent;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.impl.ProfileViewMessageBuilder;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventListenerTest {

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

    private ProfileViewEventListener listener;

    @BeforeEach
    public void setUp() {
        List<NotificationService> notificationServices = Collections.singletonList(notificationService);
        List<MessageBuilder<?>> messageBuilders = Collections.singletonList(
                new ProfileViewMessageBuilder(userServiceClient, messageSource));

        listener = new ProfileViewEventListener(objectMapper, userServiceClient,
                notificationServices, messageBuilders);
    }

    @Test
    @DisplayName("Should successfully process ProfileViewEvent and send notification")
    public void testOnMessage_Success() throws Exception {
        ProfileViewEvent event = new ProfileViewEvent();
        event.setViewerId(1L);
        event.setProfileOwnerId(2L);

        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, ProfileViewEvent.class)).thenReturn(event);

        UserDto profileOwnerDto = new UserDto();
        profileOwnerDto.setId(2L);
        profileOwnerDto.setUsername("Owner");

        UserDto viewerDto = new UserDto();
        viewerDto.setId(1L);
        viewerDto.setUsername("Viewer");

        when(userServiceClient.getUser(1L)).thenReturn(viewerDto);
        when(userServiceClient.getUser(2L)).thenReturn(profileOwnerDto);
        when(messageSource.getMessage(eq("profile.view"), any(), eq(Locale.UK))).thenReturn("Notification message");

        listener.onMessage(message, null);

        verify(notificationService, times(1)).send(eq(profileOwnerDto), eq("Notification message"));
    }

    @Test
    @DisplayName("Should throw ProfileViewException when message parsing fails")
    public void testOnMessage_EventProcessingException() throws Exception {
        byte[] messageBody = new byte[0];
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, ProfileViewEvent.class))
                .thenThrow(new IOException("Error parsing"));

        Executable executable = () -> listener.onMessage(message, new byte[0]);

        EventProcessingException exception = assertThrows(EventProcessingException.class, executable);
        assertEquals("Failed to process event of type ProfileViewEvent", exception.getMessage());
    }
}
