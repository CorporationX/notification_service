package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование методов абстрактного класса {@link AbstractMessageListener}
 *
 * @author Linempy
 * @since 15.08.2025
 */
@ExtendWith(MockitoExtension.class)
public class AbstractMessageListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userClient;

    @Mock
    private NotificationService smsService;
    @Mock
    private NotificationService emailService;
    @Mock
    private NotificationService telegramService;

    @Mock
    private MessageBuilder<EventDto> messageBuilder;

    @Mock
    private Message redisMessage;

    private List<NotificationService> services;
    private List<MessageBuilder<EventDto>> builders;

    private final String testMessage = "Тестовое сообщение";
    private final byte[] messageBody = testMessage.getBytes();

    private TestMessageListener listener;

    @BeforeEach
    public void setUp() {
        services = List.of(smsService, emailService, telegramService);
        builders = List.of(messageBuilder);

        listener = new TestMessageListener(objectMapper, userClient, services, builders);
    }

    @Test
    @DisplayName("Обработка сообщения должна выбрасывать исключение при ошибке десериализации")
    void testHandleMessage_ThrowsOnDeserializationError() throws IOException {
        when(objectMapper.readValue(messageBody, EventDto.class))
                .thenThrow(new IOException());

        Consumer<EventDto> handler = mock(Consumer.class);

        assertThrows(RuntimeException.class, () ->
                listener.handleMessage(redisMessage, EventDto.class, handler)
        );
        verify(handler, never()).accept(any());
    }

    @Test
    @DisplayName("Успешная обработка сообщения")
    void testHandleMessage_Success() throws IOException {
        EventDto expectedEvent = new EventDto();
        when(redisMessage.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, EventDto.class)).thenReturn(expectedEvent);

        Consumer<EventDto> handler = Mockito.mock(Consumer.class);

        listener.handleMessage(redisMessage, EventDto.class, handler);

        verify(handler).accept(expectedEvent);
    }

    @Test
    @DisplayName("Получение сообщения с использованием билдера")
    void testGetMessage_Success() {
        EventDto event = new EventDto();
        String expectedText = "Привет, это тест!";
        Locale locale = new Locale("ru");

        when(messageBuilder.getInstance()).thenAnswer(inv -> EventDto.class);
        when(messageBuilder.buildMessage(event, locale)).thenReturn(expectedText);

        String result = listener.getMessage(event, locale);

        assertEquals(expectedText, result);
        verify(messageBuilder).buildMessage(event, locale);
    }

    @Test
    @DisplayName("Получение сообщения должно выбрасывать исключение, если билдер не найден")
    void testGetMessage_ThrowsWhenNoBuilderFound() {
        EventDto event = new EventDto();
        Locale locale = Locale.forLanguageTag("ru");

        when(messageBuilder.getInstance()).thenAnswer(inv -> null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                listener.getMessage(event, locale)
        );
        assertTrue(exception.getMessage().contains("Билдер сообщений не найден для типа ивента: " + event.getClass().getName()));
    }

    @Test
    @DisplayName("Отправка сообщения через предпочтительный канал связи")
    void testSendMessage_Success() {
        Long userId = 1L;
        String text = "Test message";
        UserDto user = new UserDto();
        user.setId(userId);
        user.setPreference(UserDto.PreferredContact.PHONE);

        when(userClient.getUser(userId)).thenReturn(user);
        when(smsService.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);

        listener.sendMessage(userId, text);

        verify(smsService, times(1)).send(user, text);
    }

    private static class TestMessageListener extends AbstractMessageListener<EventDto> {
        public TestMessageListener(ObjectMapper objectMapper,
                                   UserServiceClient userClient,
                                   List<NotificationService> services,
                                   List<MessageBuilder<EventDto>> messageBuilders) {
            super(objectMapper, userClient, services, messageBuilders);
        }
    }

    private static class EventDto {
    }
}