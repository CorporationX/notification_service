package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.listener.LikeEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationServiceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private Map<Class<?>, MessageBuilder<?>> messageBuilders;
    @Mock
    private NotificationServiceHandler notificationServiceHandler;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private LikeEventListener likeEventListener;

    private static final String EVENT_JSON = "{\"postId\": 1, \"userId\": 2, \"authorId\": 3}";

    @BeforeEach
    void setUp() {
        likeEventListener = new LikeEventListener(objectMapper, userServiceClient, messageBuilders, notificationServiceHandler, messageSource);
    }

    @Test
    void shouldHandleLikeEventSuccessfully() throws Exception {
        LikeEvent event = new LikeEvent(1L, 2L, 3L);
        UserDto user = new UserDto();
        user.setId(3L);
        user.setUsername("test_user");
        user.setLocale("fr");

        when(objectMapper.readValue(EVENT_JSON, LikeEvent.class)).thenReturn(event);
        when(userServiceClient.getUser(3L)).thenReturn(user);
        when(messageSource.getMessage(eq("like.notification"), any(), eq(Locale.FRENCH)))
                .thenReturn("L'utilisateur 2 a aimé votre publication 1!");

        likeEventListener.handleLikeEvent(EVENT_JSON);

        verify(notificationServiceHandler).sendNotification(eq(user), eq("L'utilisateur 2 a aimé votre publication 1!"));
    }

    @Test
    void shouldHandleJsonParsingError() throws Exception {
        when(objectMapper.readValue(anyString(), eq(LikeEvent.class))).thenThrow(new RuntimeException("JSON error"));

        likeEventListener.handleLikeEvent(EVENT_JSON);

        verify(notificationServiceHandler, never()).sendNotification(any(), any());
    }

    @Test
    void shouldHandleUserServiceFailure() throws Exception {
        LikeEvent event = new LikeEvent(1L, 2L, 3L);

        when(objectMapper.readValue(EVENT_JSON, LikeEvent.class)).thenReturn(event);
        when(userServiceClient.getUser(3L)).thenThrow(new RuntimeException("User not found"));

        likeEventListener.handleLikeEvent(EVENT_JSON);

        verify(notificationServiceHandler, never()).sendNotification(any(), any());
    }

    @Test
    void shouldHandleNotificationFailure() throws Exception {
        LikeEvent event = new LikeEvent(1L, 2L, 3L);
        UserDto user = new UserDto();
        user.setId(3L);
        user.setUsername("test_user");
        user.setLocale("en");

        when(objectMapper.readValue(EVENT_JSON, LikeEvent.class)).thenReturn(event);
        when(userServiceClient.getUser(3L)).thenReturn(user);
        when(messageSource.getMessage(eq("like.notification"), any(), eq(Locale.ENGLISH)))
                .thenReturn("User 2 liked your post 1!");

        doThrow(new RuntimeException("Notification error")).when(notificationServiceHandler)
                .sendNotification(any(), any());

        likeEventListener.handleLikeEvent(EVENT_JSON);

        verify(notificationServiceHandler).sendNotification(eq(user), eq("User 2 liked your post 1!"));
    }
}