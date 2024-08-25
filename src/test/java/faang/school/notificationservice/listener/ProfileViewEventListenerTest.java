package faang.school.notificationservice.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private MessageBuilder<ProfileViewEventDto> messageBuilder;

    private ProfileViewEventListener eventListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventListener = new ProfileViewEventListener(objectMapper, userServiceClient, List.of(notificationService), List.of(messageBuilder));
    }

    @Test
    public void testOnMessage() throws IOException {
        // Arrange
        byte[] pattern = new byte[0];
        ProfileViewEventDto event = new ProfileViewEventDto();
        event.setProfileId(1L);
        String messageBody = "{\"authorId\":1}";
        when(objectMapper.readValue(messageBody.getBytes(), ProfileViewEventDto.class)).thenReturn(event);
        when(messageBuilder.getInstance()).thenReturn(ProfileViewEventDto.class);
        when(messageBuilder.buildMessage(event, Locale.US)).thenReturn("Notification message");
        when(userServiceClient.getUser(1L)).thenReturn(new UserDto());
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        // Act
        eventListener.onMessage(new Message() {
            @Override
            public byte[] getBody() {
                return messageBody.getBytes();
            }

            @Override
            public byte[] getChannel() {
                return new byte[0];
            }
        }, pattern);

        // Assert
        verify(objectMapper).readValue(messageBody.getBytes(), ProfileViewEventDto.class);
        verify(messageBuilder).buildMessage(event, Locale.US);
        verify(userServiceClient).getUser(1L);
        verify(notificationService).send(any(UserDto.class), eq("Notification message"));
    }

    @Test
    public void testOnMessageThrowsExceptionOnJsonProcessingException() throws IOException {
        // Arrange
        byte[] pattern = new byte[0];
        String messageBody = "{\"authorId\":1}";
        when(objectMapper.readValue(messageBody.getBytes(), ProfileViewEventDto.class)).thenThrow(new JsonProcessingException("Error") {});

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventListener.onMessage(new Message() {
                @Override
                public byte[] getBody() {
                    return messageBody.getBytes();
                }

                @Override
                public byte[] getChannel() {
                    return new byte[0];
                }
            }, pattern);
        });
        assertEquals("Error", exception.getCause().getMessage());
    }

    @Test
    public void testOnMessageThrowsExceptionWhenNoMessageBuilderFound() throws IOException {
        // Arrange
        byte[] pattern = new byte[0];
        ProfileViewEventDto event = new ProfileViewEventDto();
        event.setProfileId(1L);
        String messageBody = "{\"authorId\":1}";
        when(objectMapper.readValue(messageBody.getBytes(), ProfileViewEventDto.class)).thenReturn(event);
        when(messageBuilder.getInstance()).thenThrow(new IllegalArgumentException("No message builder found for " + ProfileViewEventDto.class.getName()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventListener.onMessage(new Message() {
                @Override
                public byte[] getBody() {
                    return messageBody.getBytes();
                }

                @Override
                public byte[] getChannel() {
                    return new byte[0];
                }
            }, pattern);
        });
        assertEquals("No message builder found for " + ProfileViewEventDto.class.getName(), exception.getMessage());
    }

    @Test
    public void testSentNotificationThrowsExceptionWhenNoServiceFound() {
        // Arrange
        long userId = 1L;
        String messageText = "Notification message";
        UserDto user = new UserDto();
        user.setId(userId);
        user.setPreference(UserDto.PreferredContact.EMAIL);
        user.setEmail("test@example.com");

        when(userServiceClient.getUser(userId)).thenReturn(user);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS); // Не совпадает с предпочтением

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventListener.sentNotification(userId, messageText);
        });
        assertEquals("Notification service not found, not preferred method for sending message", exception.getMessage());
    }
}
