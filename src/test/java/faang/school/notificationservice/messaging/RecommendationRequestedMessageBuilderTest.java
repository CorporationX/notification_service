package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование {@link RecommendationRequestedMessageBuilder}
 *
 * @author Linempy
 * @since 16.08.2025
 */
@DisplayName("Тестирование RecommendationRequestedMessageBuilder")
@ExtendWith(MockitoExtension.class)
public class RecommendationRequestedMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userClient;

    @InjectMocks
    private RecommendationRequestedMessageBuilder messageBuilder;

    private final Locale testLocale = Locale.US;
    private final Long requesterId = 1L;
    private final Long receiverId = 2L;
    private RecommendationRequestedEvent event;

    @BeforeEach
    public void setUp() {
        Long requestId = 1L;
        event = new RecommendationRequestedEvent(requesterId, receiverId, requestId);
    }

    @Test
    @DisplayName("getInstance должен возвращать правильный класс")
    public void getInstance_ShouldReturnCorrectClass() {
        assertEquals(RecommendationRequestedEvent.class, messageBuilder.getInstance());
    }

    @Test
    @DisplayName("buildMessage должен возвращать сообщение при валидных данных")
    public void buildMessage_ShouldReturnMessage_WhenValidData() {
        UserDto requester = new UserDto();
        requester.setUsername("JohnDoe");

        UserDto receiver = new UserDto();
        receiver.setUsername("JaneDoe");

        when(userClient.getUser(requesterId)).thenReturn(requester);
        when(userClient.getUser(receiverId)).thenReturn(receiver);

        String expectedMessage = "JaneDoe, you have a recommendation request from JohnDoe";
        when(messageSource.getMessage(
                eq("recommendation-request.new"),
                any(Object[].class),
                eq(testLocale))
        ).thenReturn(expectedMessage);

        String actualMessage = messageBuilder.buildMessage(event, testLocale);

        assertEquals(expectedMessage, actualMessage);
        verify(userClient).getUser(requesterId);
        verify(userClient).getUser(receiverId);
        verify(messageSource).getMessage(
                eq("recommendation-request.new"),
                any(Object[].class),
                eq(testLocale)
        );
    }

    @Test
    @DisplayName("buildMessage должен бросать исключение при отсутствии requester")
    public void buildMessage_ShouldThrowException_WhenRequesterNotFound() {
        when(userClient.getUser(requesterId)).thenReturn(null);
        when(userClient.getUser(receiverId)).thenReturn(new UserDto());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> messageBuilder.buildMessage(event, testLocale)
        );

        assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    @DisplayName("buildMessage должен бросать исключение при отсутствии receiver")
    public void buildMessage_ShouldThrowException_WhenReceiverNotFound() {
        when(userClient.getUser(requesterId)).thenReturn(new UserDto());
        when(userClient.getUser(receiverId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> messageBuilder.buildMessage(event, testLocale)
        );

        assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    @DisplayName("buildMessage должен корректно обрабатывать разные локали")
    public void buildMessage_ShouldHandleDifferentLocales() {
        Locale russianLocale = new Locale("ru");
        UserDto requester = new UserDto();
        requester.setUsername("ИванИванов");

        UserDto receiver = new UserDto();
        receiver.setUsername("ПетрПетров");

        when(userClient.getUser(requesterId)).thenReturn(requester);
        when(userClient.getUser(receiverId)).thenReturn(receiver);

        String expectedMessage = "ПетрПетров, у вас запрос рекомендации от ИванИванов";
        when(messageSource.getMessage(
                eq("recommendation-request.new"),
                any(Object[].class),
                eq(russianLocale))
        ).thenReturn(expectedMessage);

        String actualMessage = messageBuilder.buildMessage(event, russianLocale);

        assertEquals(expectedMessage, actualMessage);
    }
}