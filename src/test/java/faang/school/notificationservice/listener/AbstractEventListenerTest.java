package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ListenerException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import static faang.school.notificationservice.dto.UserDto.PreferredContact.SMS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
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
    private List<MessageBuilder<Object>> messageBuilders;

    @BeforeEach
    void setUp() {
        notificationServices = List.of(mock(NotificationService.class));
        MessageBuilder<Object> messageBuilder = mock(TestMessageBuilder.class);
        messageBuilders = List.of(messageBuilder);

        abstractEventListener = spy(new NotSoAbstractEventListener(userServiceClient,
                notificationServices,
                messageBuilders));
    }

    static class NotSoAbstractEventListener extends AbstractEventListener<Object> {
        public NotSoAbstractEventListener(UserServiceClient userServiceClient,
                                          List<NotificationService> notificationServices,
                                          List<MessageBuilder<Object>> messageBuilders) {
            super(null, userServiceClient, notificationServices, messageBuilders);
        }
    }

    static class TestMessageBuilder implements MessageBuilder<Object> {

        @Override
        public Class<?> getInstance() {
            return null;
        }

        @Override
        public String buildMessage(Object event, Locale locale) {
            return null;
        }
    }

    @Nested
    class PositiveTests {
        @Test
        void getMessage() {
            Class<?> eventType = Object.class;
            String message = "Message";

            messageBuilders.forEach(builder -> {
                doReturn(eventType).when(builder).getInstance();
                when(builder.buildMessage(any(), any(Locale.class))).thenReturn(message);
            });

            String actualResult = abstractEventListener.getMessage(eventType, Locale.CHINA, new Object());

            assertEquals(message, actualResult);
        }

        @Test
        void sendNotification() {
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
        void getMessage() {
            messageBuilders.forEach(builder -> doReturn(Object.class).when(builder).getInstance());

            assertThrows(ListenerException.class, () -> abstractEventListener.getMessage(Random.class, Locale.CHINA, List.of()));

            messageBuilders.forEach(builder -> {
                verify(builder, times(0)).buildMessage(any(Locale.class), any());
            });
        }
    }
}