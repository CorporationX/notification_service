package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.data.NotificationChannel;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.messaging.CommentMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @Mock
    private CommentMessageBuilder commentMessageBuilder;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    private CommentEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new CommentEventListener(commentMessageBuilder, objectMapper, userServiceClient, List.of(notificationService));
    }

    @Test
    void onMessageShouldSendNotificationWhenMessageIsValid() throws Exception {
        String json = "{\"postAuthorId\":1,\"commentAuthorId\":2,\"comment\":\"Nice post!\"}";
        CommentEvent event = CommentEvent.builder().commentId(1L).postAuthorId(2L).commentAuthorId(3L).postId(4L).build();
        UserContactsDto userContactsDto = UserContactsDto.builder()
                .id(1L)
                .email("test@example.com")
                .username("test")
                .preference(NotificationChannel.EMAIL) // Добавлено значение
                .build();
        String expectedMessage = "User 2 commented on post 1";

        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));

        when(objectMapper.readValue(json, CommentEvent.class)).thenReturn(event);
        when(userServiceClient.getUserContacts(2L)).thenReturn(userContactsDto);
        when(notificationService.getPreferredContact()).thenReturn(NotificationChannel.EMAIL);
        when(commentMessageBuilder.buildMessage(event, Locale.US)).thenReturn(expectedMessage);

        listener.onMessage(redisMessage, null);

        verify(notificationService).send(userContactsDto, expectedMessage);
    }

    @Test
    void onMessageShouldLogErrorWhenUserServiceFails() throws Exception {
        String json = "{\"postAuthorId\":1,\"commentAuthorId\":2,\"comment\":\"Nice post!\"}";
        CommentEvent event = CommentEvent.builder().commentId(1L).postAuthorId(2L).commentAuthorId(3L).postId(4L).build();

        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));

        when(objectMapper.readValue(json, CommentEvent.class)).thenReturn(event);
        when(userServiceClient.getUserContacts(1L)).thenThrow(new RuntimeException("User service unavailable"));

        listener.onMessage(redisMessage, null);

        verifyNoInteractions(notificationService, commentMessageBuilder);
    }
}