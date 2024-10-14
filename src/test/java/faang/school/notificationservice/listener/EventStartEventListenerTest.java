package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.EventStartEvent;
import faang.school.notificationservice.messaging.EventStartMessageBuilder;
import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EventStartEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private EventStartMessageBuilder messageBuilder;

    @Mock
    private Message message;

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private EventStartEventListener eventListener;

    private EventStartEvent event;
    private UserDto user;
    private final Locale locale = Locale.getDefault();
    String jsonEventStart;

    @BeforeEach
    void setup() {
        event = EventStartEvent.builder().id(1L).userIds(List.of(1L)).build();
        user = UserDto.builder().id(1L).preference(UserDto.PreferredContact.TELEGRAM).build();
        jsonEventStart = "{ \"id\": 1, \"userIds\": [2] }";
        eventListener = new EventStartEventListener(objectMapper, userServiceClient,
                messageBuilder, List.of(telegramService));
    }

    @Test
    void testOnMessageOk() throws IOException {
        when(message.getBody()).thenReturn(jsonEventStart.getBytes());
        when(objectMapper.readValue(jsonEventStart.getBytes(), EventStartEvent.class)).thenReturn(event);
        when(messageBuilder.buildMessage(event, locale)).thenReturn("babushka");
        when(userServiceClient.getUser(anyLong())).thenReturn(user);
        when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        eventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), EventStartEvent.class);
        verify(messageBuilder).buildMessage(any(), any());
        verify(userServiceClient).getUser(anyLong());
    }

    @Test
    void testNoPassingNotificationTypes() throws IOException {
        when(message.getBody()).thenReturn(jsonEventStart.getBytes());
        when(objectMapper.readValue(jsonEventStart.getBytes(), EventStartEvent.class)).thenReturn(event);
        when(messageBuilder.buildMessage(event, locale)).thenReturn("babushka");
        when(userServiceClient.getUser(anyLong())).thenReturn(user);
        when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);



        assertThrows(IllegalArgumentException.class, () -> eventListener.onMessage(message, new byte[0]));
    }
}
