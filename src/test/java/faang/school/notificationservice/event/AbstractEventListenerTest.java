package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.data.messaging.TestMessageBuilder;
import faang.school.notificationservice.event.data.service.TestEmailNotificationService;
import faang.school.notificationservice.event.data.service.TestTelegramNotificationService;
import faang.school.notificationservice.exception.EventListenerException;
import faang.school.notificationservice.exception.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Spy
    private TestEmailNotificationService testEmailNotificationService = new TestEmailNotificationService();
    @Spy
    private TestTelegramNotificationService testTelegramNotificationService = new TestTelegramNotificationService();

    private AbstractEventListener<UserDto> abstractEventListener;

    private final Consumer<UserDto> consumer = Mockito.mock(Consumer.class);

    private static final String TEST_TEXT = "test";
    private static final String EMPTY_TEST_TEXT = "test";
    private static final long USER_ID = 1L;

    @BeforeEach
    public void init() {
        this.abstractEventListener = new AbstractEventListener<>(
                objectMapper,
                userServiceClient,
                List.of(new TestMessageBuilder()),
                List.of(testEmailNotificationService, testTelegramNotificationService));
    }

    @Test
    public void givenBrokenBytes_WhenHandleEvent_ThenThrowException() throws IOException {
        byte[] bytes = TEST_TEXT.getBytes();
        when(objectMapper.readValue(bytes, UserDto.class))
                .thenThrow(new IOException());

        assertThrows(EventListenerException.class, () ->
                abstractEventListener.handleEvent(getMessage(), UserDto.class, consumer));
    }

    @Test
    public void givenBytes_WhenHandleEvent_ThenEventHandled() throws IOException {
        byte[] bytes = TEST_TEXT.getBytes();
        when(objectMapper.readValue(bytes, UserDto.class)).thenReturn(UserDto.builder().build());

        abstractEventListener.handleEvent(getMessage(), UserDto.class, consumer);

        verify(consumer, times(1)).accept(any(UserDto.class));
    }

    @Test
    public void givenUserDto_WhenGettingMessage_ThenReturnParsedMessage() {
        UserDto userDto = UserDto.builder().id(USER_ID).preference(UserDto.PreferredContact.TELEGRAM).build();

        String actualMessage = abstractEventListener.getMessage(userDto, Locale.CANADA);

        assertNotNull(actualMessage);
        assertEquals(userDto.getClass().getName(), actualMessage);
    }

    @Test
    public void givenNullUserDto_WhenSendNotification_ThenThrowException() {
        when(userServiceClient.getUser(USER_ID)).thenReturn(null);
        assertThrows(UserNotFoundException.class,
                () -> abstractEventListener.sendNotification(USER_ID, EMPTY_TEST_TEXT));
    }

    @Test
    @DisplayName("Выбрасывается исключение если юзера нет PreferredContact")
    public void givenUserDto_WhenSendNotification_ThenThrowException() {
        UserDto userDto = UserDto.builder().id(USER_ID).build();
        when(userServiceClient.getUser(USER_ID)).thenReturn(userDto);

        abstractEventListener.sendNotification(USER_ID, TEST_TEXT);

        verify(userServiceClient, times(1)).getUser(USER_ID);
        verify(testEmailNotificationService, times(1)).send(userDto, TEST_TEXT);
    }

    @Test
    @DisplayName("Успешная отправка нотификации если у пользователя PreferredContact == Email")
    public void givenUserWithEmail_WhenSendNotification_ThenMessageSend() {
        UserDto userDto = UserDto.builder().id(USER_ID).preference(UserDto.PreferredContact.EMAIL).build();
        when(userServiceClient.getUser(USER_ID)).thenReturn(userDto);

        abstractEventListener.sendNotification(USER_ID, TEST_TEXT);

        verify(userServiceClient, times(1)).getUser(USER_ID);
        verify(testEmailNotificationService, times(1)).send(userDto, TEST_TEXT);
    }

    @Test
    @DisplayName("Успешная отправка нотификации если у пользователя PreferredContact == Telegram")
    public void givenUserWithTelegram_WhenSendNotification_ThenMessageSend() {
        UserDto userDto = UserDto.builder().id(USER_ID).preference(UserDto.PreferredContact.TELEGRAM).build();
        when(userServiceClient.getUser(USER_ID)).thenReturn(userDto);

        abstractEventListener.sendNotification(USER_ID, TEST_TEXT);

        verify(userServiceClient, times(1)).getUser(USER_ID);
        verify(testTelegramNotificationService, times(1)).send(userDto, TEST_TEXT);
    }

    private Message getMessage() throws IOException {
        return new Message() {
            @Override
            public byte @NotNull [] getBody() {
                return "test".getBytes();
            }

            @Override
            public byte @NotNull [] getChannel() {
                return new byte[0];
            }
        };
    }
}
