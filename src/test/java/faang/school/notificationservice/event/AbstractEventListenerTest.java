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

    @BeforeEach
    public void init() {
        this.abstractEventListener = new AbstractEventListener<>(
                objectMapper,
                userServiceClient,
                List.of(new TestMessageBuilder()),
                List.of(testEmailNotificationService, testTelegramNotificationService));
    }

    @Test
    public void handleEventThrowExceptionTest() throws IOException {
        byte[] bytes = "test".getBytes();
        when(objectMapper.readValue(bytes, UserDto.class))
                .thenThrow(new IOException());

        assertThrows(EventListenerException.class, () ->
                abstractEventListener.handleEvent(getMessage(), UserDto.class, consumer));
    }

    @Test
    public void handleEventTest() throws IOException {
        byte[] bytes = "test".getBytes();
        when(objectMapper.readValue(bytes, UserDto.class)).thenReturn(UserDto.builder().build());

        abstractEventListener.handleEvent(getMessage(), UserDto.class, consumer);

        verify(consumer, times(1)).accept(any(UserDto.class));
    }

    @Test
    public void getMessageTest() {
        UserDto userDto = UserDto.builder().id(1L).preference(UserDto.PreferredContact.TELEGRAM).build();

        String actualMessage = abstractEventListener.getMessage(userDto, Locale.CANADA);

        assertNotNull(actualMessage);
        assertEquals(userDto.getClass().getName(), actualMessage);
    }

    @Test
    public void sendMessageThrowUserNotFoundExceptionTest() {
        when(userServiceClient.getUser(1L)).thenReturn(null);
        assertThrows(UserNotFoundException.class,
                () -> abstractEventListener.sendNotification(1L, ""));
    }

    @Test
    public void sendMessageThrowEventListenerExceptionTest() {
        when(userServiceClient.getUser(1L)).thenReturn(UserDto.builder().id(1L).build());
        assertThrows(EventListenerException.class,
                () -> abstractEventListener.sendNotification(1L, ""));
    }

    @Test
    public void sendMessageWithPreferredEmailTest() {
        UserDto userDto = UserDto.builder().id(1L).preference(UserDto.PreferredContact.EMAIL).build();
        when(userServiceClient.getUser(1L)).thenReturn(userDto);

        abstractEventListener.sendNotification(1L, "test");

        verify(userServiceClient, times(1)).getUser(1L);
        verify(testEmailNotificationService, times(1)).send(userDto, "test");
    }

    @Test
    public void sendMessageWithPreferredTelegramTest() {
        UserDto userDto = UserDto.builder().id(1L).preference(UserDto.PreferredContact.TELEGRAM).build();
        when(userServiceClient.getUser(1L)).thenReturn(userDto);

        abstractEventListener.sendNotification(1L, "test");

        verify(userServiceClient, times(1)).getUser(1L);
        verify(testTelegramNotificationService, times(1)).send(userDto, "test");
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
