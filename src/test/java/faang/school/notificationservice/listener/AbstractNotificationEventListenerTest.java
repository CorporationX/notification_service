package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AbstractNotificationEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private List<NotificationService> notificationList;
    @Mock
    private List<MessageBuilder<TestEvent>> messageBuilders;
    @Mock
    private MessageBuilder<TestEvent> messageBuilder;

    private TestEventListener testEventListener;

    @BeforeEach
    void setUp() {
        testEventListener = new TestEventListener(objectMapper, notificationList, messageBuilders);
    }

    @Test
    void testGetMessageSuccessful() {
        TestEvent testEvent = new TestEvent();
        Locale locale = Locale.US;
        String expectedMessage = "Test message";

        doReturn(TestEvent.class).when(messageBuilder).getInstance();
        when(messageBuilder.buildMessage(testEvent, locale)).thenReturn(expectedMessage);
        when(messageBuilders.stream()).thenReturn(Stream.of(messageBuilder));

        String actualMessage = testEventListener.getMessage(testEvent, locale);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetMessageThrowsException() {
        TestEvent testEvent = new TestEvent();
        Locale locale = Locale.US;

        doReturn(Stream.of()).when(messageBuilders).stream();

        assertThrows(IllegalArgumentException.class, () -> testEventListener.getMessage(testEvent, locale));
    }


    @Test
    void testSendNotificationThrowsException() {
        UserDto userDto = new UserDto();
        String message = "Test notification";
        doReturn(Stream.of()).when(notificationList).stream();

        assertThrows(IllegalArgumentException.class, () -> testEventListener.sendNotification(userDto, message));
    }

    private static class TestEventListener extends AbstractEventListener<TestEvent> {
        public TestEventListener(ObjectMapper objectMapper,
                               List<NotificationService> notificationList,
                               List<MessageBuilder<TestEvent>> messageBuilders) {
            super(objectMapper, notificationList, messageBuilders);
        }
    }

    protected static class TestEvent {
    }

}
