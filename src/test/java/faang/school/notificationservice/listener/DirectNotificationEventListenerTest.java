package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DirectNotificationEventListenerTest {

    @Mock
    private NotificationSenderService notificationSender;
    @Mock
    private MessageBuilder<TestEvent> messageBuilder;

    private TestEventListener testEventListener;

    @BeforeEach
    void setUp() {
        testEventListener = new TestEventListener(notificationSender, messageBuilder);
    }

    @Test
    void testSendNotificationWhenEventIsValid() {
        UserDto owner = new UserDto();
        TestEvent validEvent = new TestEvent(owner, true);
        String expectedMessage = "Test Message";

        when(messageBuilder.buildMessage(any(), any())).thenReturn(expectedMessage);

        testEventListener.sendNotification(validEvent);

        verify(notificationSender).send(owner, expectedMessage);
        verify(messageBuilder).buildMessage(validEvent, LocaleContextHolder.getLocale());
    }

    @Test
    void testSendNotificationWhenEventIsInvalid() {
        TestEvent invalidEvent = new TestEvent(new UserDto(), false);

        testEventListener.sendNotification(invalidEvent);

        verify(notificationSender, never()).send(any(), any());
        verify(messageBuilder, never()).buildMessage(any(), any());
    }

    private static class TestEventListener extends DirectNotificationEventListener<TestEvent> {
        public TestEventListener(
                NotificationSenderService notificationSender,
                MessageBuilder<TestEvent> messageBuilder) {
            super(notificationSender, messageBuilder);
        }

        @Override
        public boolean isEventValid(TestEvent event) {
            return event.valid();
        }
    }

    protected record TestEvent(UserDto getOwner, @Getter boolean valid) implements NotificationEvent {
    }
}