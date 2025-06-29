package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractNotificationEventListenerTest {

    @Mock
    private List<NotificationService> notificationList;
    @Mock
    private MessageBuilder<TestEvent> messageBuilder;

    private TestEventListener testEventListener;

    @BeforeEach
    void setUp() {
        testEventListener = new TestEventListener(notificationList, messageBuilder);
    }

    @Test
    void testGetMessageSuccessful() {
        TestEvent testEvent = new TestEvent();
        Locale locale = Locale.US;
        String expectedMessage = "Test message";

        when(messageBuilder.buildMessage(testEvent, locale)).thenReturn(expectedMessage);

        String actualMessage = testEventListener.getMessage(testEvent, locale);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testSendNotificationThrowsException() {
        String message = "Test notification";
        TestEvent event = new TestEvent();

        doReturn(Stream.of()).when(notificationList).stream();

        assertThrows(IllegalArgumentException.class, () -> testEventListener.sendNotification(event, message));
    }

    private static class TestEventListener extends AbstractEventListener<TestEvent> {
        public TestEventListener(
                               List<NotificationService> notificationList,
                               MessageBuilder<TestEvent> messageBuilder) {
            super(notificationList, messageBuilder);
        }

        @Override
        protected boolean isEventValid(TestEvent event) {
            return true;
        }
    }

    protected static class TestEvent implements NotificationEvent {
        @Override
        public UserDto getOwner() {
            return new UserDto();
        }
    }
}