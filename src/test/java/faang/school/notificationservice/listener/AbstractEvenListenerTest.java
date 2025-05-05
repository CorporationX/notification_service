package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.InvalidPreferredContactException;
import faang.school.notificationservice.exception.UnsupportedLocaleException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.PHONE;
import static faang.school.notificationservice.dto.UserDto.PreferredContact.TELEGRAM;
import static faang.school.notificationservice.messages.ErrorMessages.NO_MESSAGE_BUILDER_FOUND_FOR_LOCALE;
import static faang.school.notificationservice.messages.ErrorMessages.NO_NOTIFICATION_SERVICE_FOUND_FOR_PREFERRED_COMMUNICATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
public class AbstractEvenListenerTest {
    static class TestEvent {
        public String value;
    }

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<TestEvent> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Message message;

    @Mock
    private Consumer<TestEvent> consumer;

    @Mock
    private ObjectMapper objectMapper;

    private final TestEvent testEvent = new TestEvent();
    private AbstractEvenListener<TestEvent> abstractEvenListener;

    @BeforeEach
    public void setup() {
        testEvent.value = "test";
        abstractEvenListener = new AbstractEvenListener<>(
                objectMapper,
                userServiceClient,
                List.of(messageBuilder),
                List.of(notificationService)
        ) {
        };
    }

    @Test
    public void testProcessEvent_success() throws Exception {
        String json = "{\"value\": \"test\"}";
        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(eq(json.getBytes(StandardCharsets.UTF_8)), (Class<Object>) any())).thenReturn(testEvent);

        abstractEvenListener.processEvent(message, TestEvent.class, consumer);

        verify(consumer).accept(any(TestEvent.class));
    }

    @Test
    public void testGetMessage_success() {
        Locale locale = Locale.ENGLISH;
        String expected = "Test message";
        doReturn(testEvent.getClass()).when(messageBuilder).getInstance();
        when(messageBuilder.buildMessage(testEvent, locale)).thenReturn(expected);

        String result = abstractEvenListener.getMessage(testEvent, locale);

        assertEquals(expected, result);
    }

    @Test
    public void testGetMessage_unsupportedLocale() {
        Locale locale = Locale.ENGLISH;
        String expected = "Test message";
        doReturn(expected.getClass()).when(messageBuilder).getInstance();

        UnsupportedLocaleException exception = assertThrows(UnsupportedLocaleException.class,
                () -> abstractEvenListener.getMessage(testEvent, locale)
        );

        assertEquals(NO_MESSAGE_BUILDER_FOUND_FOR_LOCALE.formatted(locale), exception.getMessage());
    }

    @Test
    public void testSendNotification_success() {
        UserDto userDto = new UserDto(1L, "name", "test@mail.test", "phone", TELEGRAM, Locale.ENGLISH);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(TELEGRAM);

        abstractEvenListener.sendNotification(1L, "message");

        verify(notificationService).send(userDto, "message");
    }

    @Test
    public void testSendNotification_invalidPreferredContact() {
        UserDto userDto = new UserDto(2L, "name", "test@mail.test", "phone", PHONE, Locale.ENGLISH);
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(TELEGRAM);

        InvalidPreferredContactException exception = assertThrows(InvalidPreferredContactException.class,
                () -> abstractEvenListener.sendNotification(2L, "message2")
        );
        assertEquals(NO_NOTIFICATION_SERVICE_FOUND_FOR_PREFERRED_COMMUNICATION.formatted(userDto.getPreference()),
                exception.getMessage());
    }
}
