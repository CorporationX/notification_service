package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.messaging.FollowerEventMessageBuilder;
import faang.school.notificationservice.model.event.FollowerEventDto;
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

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private FollowerEventMessageBuilder messageBuilder;

    @Mock
    private Message message;

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private FollowerEventListener eventListener;

    private FollowerEventDto event;
    private UserDto user;
    private Locale locale = Locale.getDefault();
    private String jsonProjectFollowerEvent;

    @BeforeEach
    void setup() {
        jsonProjectFollowerEvent = "{ \"followerId\": 1, \"followeeId\": 2, \"subscribedAt\": null }";
        user = UserDto.builder().id(1L).preference(UserDto.PreferredContact.TELEGRAM).build();
        eventListener = new FollowerEventListener(objectMapper, userServiceClient, messageBuilder, List.of(telegramService));

        event = FollowerEventDto.builder()
                .followerId(1L)
                .followeeId(2L)
                .subscribedAt(null)
                .build();
    }

    @Test
    void testOnMessageOk() throws IOException {
        when(message.getBody()).thenReturn(jsonProjectFollowerEvent.getBytes());
        when(objectMapper.readValue(jsonProjectFollowerEvent.getBytes(), FollowerEventDto.class)).thenReturn(event);
        when(messageBuilder.buildMessage(event, locale)).thenReturn("babushka");
        when(userServiceClient.getUser(anyLong())).thenReturn(user);
        when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        eventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), FollowerEventDto.class);
        verify(messageBuilder).buildMessage(any(), any());
        verify(userServiceClient).getUser(anyLong());
    }
}
