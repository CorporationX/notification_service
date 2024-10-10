package faang.school.notificationservice.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.listener.ProjectFollowerEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.ProjectFollowerMessageBuilder;
import faang.school.notificationservice.model.event.ProjectFollowerEvent;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;

@ExtendWith(MockitoExtension.class)
public class ProjectFollowerMessageBuilderTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ProjectFollowerMessageBuilder messageBuilder;

    @Mock
    private List<NotificationService> notifications;

    @Mock
    private Message message;

    @InjectMocks
    private ProjectFollowerEventListener eventListener;

    private ProjectFollowerEvent event;
    private UserDto user;
    private Locale locale = Locale.getDefault();

    @BeforeEach
    void setup(){
        event = ProjectFollowerEvent.builder().build();
        user = UserDto.builder().id(1L).build();
        notifications = new ArrayList<>();
    }

//    @Test
//    void testOnMessageOk() throws IOException {
//        when(objectMapper.readValue(message.getBody(), ProjectFollowerEvent.class)).thenReturn(event);
//        when(messageBuilder.buildMessage(event, locale)).thenReturn("babushka");
//        when(userServiceClient.getUser(anyLong())).thenReturn(user);
//
//        eventListener.onMessage(message, new byte[0]);
//
//        verify(objectMapper).readValue(message.getBody(), ProjectFollowerEvent.class);
//        verify(messageBuilder).buildMessage(any(), any());
//        verify(userServiceClient).getUser(anyLong());
//    }
}
