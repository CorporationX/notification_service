package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
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
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventListenerHandlerTest {

    @InjectMocks
    private EventListenerHandler<Object> eventListenerHandler;

    @Mock
    private MessageBuilder<Object> messageBuilder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Function<Object, UserDto> handle;

    @Mock
    private Message message;

    private List<MessageBuilder<Object>> messageBuilders;
    private List<NotificationService> notificationServices;

    @BeforeEach
    public void setUp() {
        messageBuilders = List.of(messageBuilder, messageBuilder);
        notificationServices = List.of(notificationService, notificationService);

        eventListenerHandler = new EventListenerHandler<>(
                messageBuilders,
                notificationServices,
                objectMapper);
    }

    @Test
    public void testEventHandler() throws IOException {
        Object event = new Object();
        UserDto user = new UserDto();
        user.setLocale(new Locale("ru", "RU"));
        user.setPreference(UserDto.PreferredContact.SMS);

        String text = "text";
        byte[] bytes = new byte[0];

        when(message.getBody()).thenReturn(bytes);

        when(objectMapper.readValue(bytes, Object.class)).thenReturn(event);
        when(handle.apply(event)).thenReturn(user);
        doReturn(Object.class).when(messageBuilder).getInstance();
        when(messageBuilder.buildMessage(any(), any())).thenReturn(text);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        eventListenerHandler.eventHandler(message, Object.class, handle);

        verify(notificationService).send(user, text);

    }
}
