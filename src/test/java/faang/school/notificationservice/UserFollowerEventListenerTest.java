package faang.school.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.listener.UserFollowerEventListener;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.UserFollowerEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.impl.UserFollowerMessageBuilder;
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
public class UserFollowerEventListenerTest {

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

    private UserFollowerEventListener listener;

    @BeforeEach
    public void setUp() {
        List<NotificationService> notificationServices = Collections.singletonList(notificationService);
        List<MessageBuilder<?>> messageBuilders = Collections.singletonList(
                new UserFollowerMessageBuilder(userServiceClient, messageSource));

        listener = new UserFollowerEventListener(objectMapper, userServiceClient,
                notificationServices, messageBuilders);
    }

    @Test
    @DisplayName("Should successfully process UserFollowerEvent and send notification")
    public void testOnMessage_Success() throws Exception {
        UserFollowerEvent event = new UserFollowerEvent();
        event.setFollowerId(1L);
        event.setFollowedUserId(3L);

        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, UserFollowerEvent.class)).thenReturn(event);

        UserDto followedUserDto = new UserDto();
        followedUserDto.setId(3L);
        followedUserDto.setUsername("Followed user");

        when(userServiceClient.getUser(3L)).thenReturn(followedUserDto);
        when(userServiceClient.getUser(1L)).thenReturn(new UserDto());
        when(messageSource.getMessage(eq("new.follower"), any(), eq(Locale.UK))).thenReturn("Notification message");

        listener.onMessage(message, null);

        verify(notificationService, times(1)).send(eq(followedUserDto), eq("Notification message"));
    }

    @Test
    @DisplayName("Should throw EventProcessingException when message parsing fails")
    public void testOnMessage_EventProcessingException() throws Exception {
        byte[] messageBody = new byte[0];
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, UserFollowerEvent.class))
                .thenThrow(new IOException("Error parsing"));

        Executable executable = () -> listener.onMessage(message, new byte[0]);

        EventProcessingException exception = assertThrows(EventProcessingException.class, executable);
        assertEquals("Failed to process event of type UserFollowerEvent", exception.getMessage());
    }
}