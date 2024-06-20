package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.SMS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {
    private NotSoAbstractEventListener abstractEventListener;
    @Mock
    private UserServiceClient userServiceClient;
    private List<NotificationService> notificationServices;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        notificationServices = List.of(mock(NotificationService.class));
        objectMapper = mock(ObjectMapper.class);

        abstractEventListener = spy(new NotSoAbstractEventListener(objectMapper,
                userServiceClient,
                notificationServices,
                null));
    }

    static class NotSoAbstractEventListener extends AbstractEventListener<Object> {
        public NotSoAbstractEventListener(ObjectMapper objectMapper,
                                          UserServiceClient userServiceClient,
                                          List<NotificationService> notificationServices,
                                          MessageBuilder<Object> messageBuilder) {
            super(objectMapper, userServiceClient, notificationServices, messageBuilder);
        }
    }

    static class TestMessageBuilder implements MessageBuilder<Object> {
        @Override
        public String buildMessage(Object event, Locale locale) {
            return "";
        }
    }

    @Nested
    class PositiveTests {
        @Test
        void mapMessageBodyToEventTest() throws IOException {
            byte[] messageBody = new byte[]{};
            abstractEventListener.mapMessageBodyToEvent(messageBody, Object.class);

            verify(objectMapper).readValue(messageBody, Object.class);
        }

        @Test
        void sendNotificationTest() {
            String message = "Message";
            UserDto userDto = UserDto.builder()
                    .id(1L)
                    .email("email@gmail.com")
                    .phone("12345678")
                    .build();

            userDto.setPreference(SMS);
            when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
            notificationServices.forEach(notificationService -> when(notificationService.getPreferredContact()).thenReturn(SMS));

            abstractEventListener.sendNotification(1L, message);

            notificationServices.forEach(notificationService -> verify(notificationService).send(any(UserDto.class), eq(message)));
        }
    }

    @Nested
    class NegativeTests {
        @Test
        void sendNotificationTest() {
            String message = "Message";
            UserDto userDto = UserDto.builder()
                    .id(1L)
                    .email("email@gmail.com")
                    .phone("12345678")
                    .build();
            userDto.setPreference(null);

            when(userServiceClient.getUser(anyLong())).thenReturn(userDto);

            abstractEventListener.sendNotification(1L, message);

            notificationServices.forEach(notificationService ->
                    verify(notificationService, times(0)).send(any(UserDto.class), eq(message)));
        }
    }
}