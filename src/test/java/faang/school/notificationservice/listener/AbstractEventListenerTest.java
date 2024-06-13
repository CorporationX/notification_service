package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ListenerException;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.SMS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {
    @InjectMocks
    private NotSoAbstractEventListener abstractEventListener;
    @Mock
    private UserServiceClient userServiceClient;
    private List<NotificationService> notificationServices;
    private List<MessageBuilder<?>> messageBuilders;

    @BeforeEach
    void setUp() {
        notificationServices = List.of(mock(NotificationService.class));
        MessageBuilder<?> messageBuilder = mock(MessageBuilder.class);
        messageBuilders = List.of(messageBuilder);

        abstractEventListener.setMessageBuilders(messageBuilders);
        abstractEventListener.setNotificationServices(notificationServices);
    }

    static class NotSoAbstractEventListener extends AbstractEventListener {
        public NotSoAbstractEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient) {
            super(objectMapper, userServiceClient);
        }
    }

    @Nested
    class PositiveTests {
        @Test
        void getMessage() {
            Class<?> eventType = Object.class;
            String message = "Message";

            messageBuilders.forEach(builder -> {
                doReturn(eventType).when(builder).getEventType();
                when(builder.buildMessage(any(Locale.class), anyList())).thenReturn(message);
            });

            String actualResult = abstractEventListener.getMessage(eventType, Locale.CHINA, List.of());

            assertEquals(message, actualResult);
        }

        @Test
        void sendNotification() {
            String message = "Message";
            UserDto userDto = new UserDto();
            userDto.setPreference(SMS);
            when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
            notificationServices.forEach(notificationService -> when(notificationService.getPreferredContact()).thenReturn(SMS));

            abstractEventListener.sendNotification(anyLong(), message);

            notificationServices.forEach(notificationService -> verify(notificationService).send(any(UserDto.class), eq(message)));
        }
    }

    @Nested
    class NegativeTests {
        @Test
        void getMessage() {
            messageBuilders.forEach(builder -> doReturn(Object.class).when(builder).getEventType());

            assertThrows(ListenerException.class, () -> abstractEventListener.getMessage(Random.class, Locale.CHINA, List.of()));

            messageBuilders.forEach(builder -> {
                verify(builder, times(0)).buildMessage(any(Locale.class), anyList());
            });
        }
    }
}