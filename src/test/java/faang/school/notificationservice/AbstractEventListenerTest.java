package faang.school.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private List<NotificationService> notificationServices;

    @Mock
    private List<MessageBuilder<Object>> messageBuilders;

    @InjectMocks
    private AbstractEventListener<Object> abstractEventListener;

    @BeforeEach
    void setUp() {
        abstractEventListener = new AbstractEventListener<>(objectMapper,
                userServiceClient,
                notificationServices,
                messageBuilders);
    }

    @Test
    @DisplayName("Успешная обработка сообщения")
    void handleMessageSuccess() throws IOException {
        Message mockMessage = mock(Message.class);
        byte[] mockBody = "test".getBytes();
        when(mockMessage.getBody()).thenReturn(mockBody);

        Object expectedObject = new Object();
        when(objectMapper.readValue(any(byte[].class), eq(Object.class))).thenReturn(expectedObject);

        Object result = abstractEventListener.handleMessage(mockMessage, Object.class);

        assertNotNull(result);
        assertEquals(expectedObject, result);
    }

    @Test
    @DisplayName("Не найден message builder")
    void messageBuilderNotFound() {
        Object mockEvent = new Object();
        when(messageBuilders.stream()).thenReturn(Stream.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                abstractEventListener.getMessage(mockEvent, Locale.ENGLISH)
        );

        assertEquals("Not found messageBuilder for type: class java.lang.Object", exception.getMessage());
    }

    @Test
    @DisplayName("Успешная отправка нотификации")
    void sendNotificationSuccess() {
        UserDto userDto = new UserDto();
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        String message = "Test Message";

        NotificationService mockNotificationService = mock(NotificationService.class);
        when(mockNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        when(notificationServices.stream()).thenReturn(Stream.of(mockNotificationService));

        abstractEventListener.sendNotification(userDto, message);

        verify(mockNotificationService, times(1)).send(userDto, message);
    }
}