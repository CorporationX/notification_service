package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.ProjectFollowerMessageBuilder;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.ProjectFollowerEvent;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectFollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ProjectFollowerMessageBuilder messageBuilder;

    @Mock
    private Message message;

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private ProjectFollowerEventListener eventListener;

    private ProjectFollowerEvent event;
    private UserDto user;
    private Locale locale = Locale.getDefault();
    private String jsonProjectFollowerEvent;

    @BeforeEach
    void setup() {
        jsonProjectFollowerEvent = "{ \"ownerId\": 1, \"followerId\": 2, \"projectId\": 3 }";
        user = UserDto.builder().id(1L).preference(UserDto.PreferredContact.TELEGRAM).build();
        eventListener = new ProjectFollowerEventListener(objectMapper, userServiceClient, messageBuilder, List.of(telegramService));

        event = ProjectFollowerEvent.builder()
                .ownerId(1L)
                .followerId(2L)
                .projectId(3L)
                .build();
    }

    @Test
    void testOnMessageOk() throws IOException {
        when(message.getBody()).thenReturn(jsonProjectFollowerEvent.getBytes());
        when(objectMapper.readValue(jsonProjectFollowerEvent.getBytes(), ProjectFollowerEvent.class)).thenReturn(event);
        when(messageBuilder.buildMessage(event, locale)).thenReturn("babushka");
        when(userServiceClient.getUser(anyLong())).thenReturn(user);
        when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        eventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), ProjectFollowerEvent.class);
        verify(messageBuilder).buildMessage(any(), any());
        verify(userServiceClient).getUser(anyLong());
    }
}
