package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerByDimaTest {
    @Mock
    private NotificationService notificationService;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private MessageBuilder<Object> messageBuilder;
    private Class<Object> eventType;
    private AbstractEventListenerByDima<Object> eventListener;
    private UserDto userDto = new UserDto();
    private Object[] args = new Object[0];
    private Message message = new Message() {
        @Override
        public byte[] getBody() {
            return new byte[0];
        }
        @Override
        public byte[] getChannel() {
            return new byte[0];
        }
    };


    @BeforeEach
    public void setUp() {
        eventListener = new AbstractEventListenerByDima<Object>(
                List.of(notificationService),
                mapper, messageBuilder, eventType) {
            @Override
            protected List<UserDto> getNotifiedUsers(Object event) {
                return List.of(userDto);
            }

            @Override
            protected Object[] getArgs(Object event) {
                return args;
            }
        };
    }

    @Test
    public void shouldReturnFalseCanBeNotified() {
        assertFalse(eventListener.canBeNotified(notificationService, userDto));
    }

    @Test
    public void shouldReturnTrueCanBeNotified() {
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        assertTrue(eventListener.canBeNotified(notificationService, userDto));
    }

    @Test
    public void testOnMessage() throws IOException {
        Object event = new Object();
        when(mapper.readValue(message.getBody(), eventType)).thenReturn(event);
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        String text = "Text";
        when(messageBuilder.buildMessage(event, Locale.US, args)).thenReturn(text);

        eventListener.onMessage(message, new byte[0]);

        verify(notificationService).send(userDto, text);
    }

}
