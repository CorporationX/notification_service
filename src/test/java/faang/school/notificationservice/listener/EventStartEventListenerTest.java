package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.model.event.EventStartEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class EventStartEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageBuilder<EventStartEvent> messageBuilder;

    @Mock
    private List<NotificationService> notifications;

    @Mock
    private Message message;

    @InjectMocks
    private EventStartEventListener eventListener;

    private EventStartEvent event;
    private UserDto user;
    private Locale locale = Locale.getDefault();

    @BeforeEach
    void setup(){
        event = EventStartEvent.builder().id(1L).userIds(List.of(1L)).build();
        user = UserDto.builder().id(1L).build();
        notifications = new ArrayList<>();
    }


}
