package faang.school.notificationservice.listener;


import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.ProfileViewEventDto;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.subscriber.ProfileViewEventListener;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageBuilder<ProfileViewEventDto> messageBuilder;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Message message;

    private ProfileViewEventListener listener;

    @BeforeEach
    public void setUp() {
        List<NotificationService> notificationServices = Collections.singletonList(notificationService);
        listener = new ProfileViewEventListener(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Test
    public void testOnMessage_Success() throws IOException {
        // Mock data
        ProfileViewEventDto event = ProfileViewEventDto
                .builder()
                .senderId(1L)
                .receiverId(2L)
                .dateTime(LocalDateTime.now())
                .build();
        String expectedMessage = "Someone viewed your profile!";
        UserDto user = UserDto
                .builder()
                .id(2L)
                .username("John Doe")
                .email("john.doe@example.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(objectMapper.readValue(message.getBody(), ProfileViewEventDto.class)).thenReturn(event);
        when(messageBuilder.buildMessage(event, Locale.getDefault())).thenReturn(expectedMessage);
        when(userServiceClient.getUser(event.getReceiverId())).thenReturn(user);
        when(notificationService.getPreferredContact()).thenReturn(user.getPreference());

        // Call the method
        listener.onMessage(message, null);

        // Verify interactions
        verify(objectMapper).readValue(message.getBody(), ProfileViewEventDto.class);
        verify(messageBuilder).buildMessage(event, Locale.getDefault());
        verify(userServiceClient).getUser(event.getReceiverId());
        verify(notificationService).send(user, expectedMessage);
    }

    @Test
    public void shouldThrowDataValidationException_whenEventReadFails() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProfileViewEventDto.class)).thenThrow(new IOException("Error"));
        Assert.assertThrows(DataValidationException.class, () -> listener.onMessage(message, null));
        verify(objectMapper).readValue(message.getBody(), ProfileViewEventDto.class);
        verifyNoInteractions(userServiceClient);
        verifyNoInteractions(messageBuilder);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void testOnMessage_NoMatchingNotificationService() throws IOException {
        // Mock data
        ProfileViewEventDto event = ProfileViewEventDto
                .builder()
                .senderId(1L)
                .receiverId(2L)
                .dateTime(LocalDateTime.now())
                .build();
        String expectedMessage = "Someone viewed your profile!";
        UserDto user = UserDto.builder()
                .id(2L)
                .username("John Doe")
                .email("john.doe@example.com")
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(objectMapper.readValue(message.getBody(), ProfileViewEventDto.class)).thenReturn(event);
        when(messageBuilder.buildMessage(event, Locale.getDefault())).thenReturn(expectedMessage);
        when(userServiceClient.getUser(event.getReceiverId())).thenReturn(user);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS); // Doesn't match user preference

        // Call the method
        listener.onMessage(message, null);

        // Verify interactions
        verify(objectMapper).readValue(message.getBody(), ProfileViewEventDto.class);
        verify(messageBuilder).buildMessage(event, Locale.getDefault());
        verify(userServiceClient).getUser(event.getReceiverId());
        verify(notificationService).getPreferredContact();
        verify(notificationService, never()).send(any(), any());
        verify(userServiceClient, never()).getUser(event.getReceiverId());
    }
}
