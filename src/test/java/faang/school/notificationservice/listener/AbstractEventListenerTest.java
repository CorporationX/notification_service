package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    private AbstractEventListener<Object> eventListener;

    @Mock
    ObjectMapper objectMapper;

    @Test
    void testSendMessageSuccess() throws IOException {

        UserDto notifiedUser = UserDto.builder()
                .id(1L)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        StringBuilder actual = new StringBuilder();

        String expectedMessage = "Test message";

        AbstractEventListener<Object> abstractEventListener = new AbstractEventListener<Object>(
                List.of(
                        new NotificationService() {
                            @Override
                            public void send(UserDto user, String message) {
                                actual.append(message);
                            }

                            @Override
                            public UserDto.PreferredContact getPreferredContact() {
                                return UserDto.PreferredContact.EMAIL;
                            }
                        }
                ),
                objectMapper,
                new MessageBuilder<>() {
                    @Override
                    public Class<?> getInstance() {
                        return Object.class;
                    }

                    @Override
                    public String buildMessage(Object event, Locale locale, Object[] args) {
                        return expectedMessage;
                    }
                },
                Object.class

        ) {
            @Override
            protected List<UserDto> getNotifiedUsers(Object event) {
                return List.of(notifiedUser);
            }
        };

        Message eventMsg = new DefaultMessage(new byte[]{}, new byte[]{});

        Object event = new Object();

        when(objectMapper.readValue(eventMsg.getBody(), Object.class)).thenReturn(
                event
        );

        abstractEventListener.onMessage(eventMsg, new byte[]{});

        assertEquals(expectedMessage, actual.toString());
    }
}
