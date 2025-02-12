package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventListenerImplTest {

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
    private EventListenerImpl eventListener;

    private static final String EVENT_JSON = "{\"userId\": 1, \"username\": \"test_user\", \"email\": \"test@example.com\"}";

    @BeforeEach
    void setUp() {
        eventListener = new EventListenerImpl(objectMapper, userServiceClient, messageBuilders, notificationServiceHandler, messageSource);
    }

    @Test
    void shouldProcessEventSuccessfully() throws Exception {
        UserRegisteredEvent event = new UserRegisteredEvent();
        event.setUserId(1L);
        event.setUsername("test_user");
        event.setEmail("test@example.com");

        UserDto user = new UserDto();
        user.setId(1L);
        user.setUsername("test_user");
        user.setLocale("ru");

        when(objectMapper.readValue(EVENT_JSON, UserRegisteredEvent.class)).thenReturn(event);
        when(userServiceClient.getUser(1L)).thenReturn(user);
        when(messageSource.getMessage(eq("follower.new"), any(), eq(Locale.forLanguageTag("ru"))))
                .thenReturn("Поздравляем! У вас новый подписчик!");

        eventListener.processEvent(EVENT_JSON);

        verify(notificationServiceHandler).sendNotification(eq(user), eq("Поздравляем! У вас новый подписчик!"));
    }

    @Test
    void shouldHandleJsonParsingError() throws Exception {
        when(objectMapper.readValue(anyString(), eq(UserRegisteredEvent.class))).thenThrow(new RuntimeException("JSON error"));

        eventListener.processEvent(EVENT_JSON);

        verify(notificationServiceHandler, never()).sendNotification(any(), any());
    }

    @Test
    void shouldHandleUserServiceFailure() throws Exception {
        UserRegisteredEvent event = new UserRegisteredEvent();
        event.setUserId(1L);

        when(objectMapper.readValue(EVENT_JSON, UserRegisteredEvent.class)).thenReturn(event);
        when(userServiceClient.getUser(1L)).thenThrow(new RuntimeException("User not found"));

        eventListener.processEvent(EVENT_JSON);

        verify(notificationServiceHandler, never()).sendNotification(any(), any());
    }

    @Test
    void shouldHandleNotificationFailure() throws Exception {
        UserRegisteredEvent event = new UserRegisteredEvent();
        event.setUserId(1L);
        event.setUsername("test_user");

        UserDto user = new UserDto();
        user.setId(1L);
        user.setUsername("test_user");
        user.setLocale("en");

        when(objectMapper.readValue(EVENT_JSON, UserRegisteredEvent.class)).thenReturn(event);
        when(userServiceClient.getUser(1L)).thenReturn(user);
        when(messageSource.getMessage(eq("follower.new"), any(), eq(Locale.ENGLISH)))
                .thenReturn("Congrats! You've got a new follower!");

        doThrow(new RuntimeException("Notification error")).when(notificationServiceHandler)
                .sendNotification(any(), any());

        eventListener.processEvent(EVENT_JSON);

        verify(notificationServiceHandler).sendNotification(eq(user), eq("Congrats! You've got a new follower!"));
    }
}