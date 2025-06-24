package faang.school.notificationservice.event;

import faang.school.notificationservice.client.FeignUserServiceAdapter;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.FetchViaFeignException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.NotificationServiceNotFoundException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {
    @Mock
    private FeignUserServiceAdapter feignUserServiceAdapter;
    @Mock
    private MessageBuilder<TestEvent> messageBuilder;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Consumer<TestEvent> consumer;

    private AbstractEventListener<TestEvent> eventListener;

    private static class TestEventListener extends AbstractEventListener<TestEvent> {
        public TestEventListener(MessageBuilder<TestEvent> messageBuilders,
                                 List<NotificationService> notificationServices,
                                 FeignUserServiceAdapter feignUserServiceAdapter) {
            super(messageBuilders, notificationServices, feignUserServiceAdapter);
        }
    }

    private static class TestEvent extends Event {
        @Override
        public String getEventType() {
            return "TEST_EVENT";
        }
    }

    private static class AnotherTestEvent extends TestEvent {
        @Override
        public String getEventType() {
            return "ANOTHER_TEST_EVENT";
        }
    }

    @BeforeEach
    void setUp() {
        eventListener = new TestEventListener(
                messageBuilder,
                List.of(notificationService),
                feignUserServiceAdapter
        );
        when(notificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        eventListener.init();
    }

    @Test
    void testHandleEvent_Success() {
        TestEvent testEvent = new TestEvent();
        eventListener.handleEvent(testEvent, consumer);
        verify(consumer).accept(testEvent);
    }

    @Test
    void testGetMessage_Success() {
        TestEvent testEvent = new TestEvent();
        String expectedMessage = "Test Message";
        Locale locale = Locale.getDefault();

        when(messageBuilder.buildMessage(testEvent, locale)).thenReturn(expectedMessage);

        String actualMessage = eventListener.getMessage(testEvent, locale);

        assertEquals(expectedMessage, actualMessage);
        verify(messageBuilder).buildMessage(testEvent, locale);
    }

    @Test
    void testGetMessage_BuilderNotFound() {
        eventListener = new TestEventListener(
                null,
                List.of(notificationService),
                feignUserServiceAdapter
        );
        AnotherTestEvent anotherTestEvent = new AnotherTestEvent();
        Locale locale = Locale.getDefault();
        assertThrows(MessageBuilderNotFoundException.class, () -> eventListener.getMessage(anotherTestEvent, locale));
    }

    @Test
    void testSendNotification_Success() {
        UserDto user = new UserDto();
        user.setUsername("testuser");
        user.setPreference(UserDto.PreferredContact.EMAIL);
        String message = "Hello, testuser!";

        eventListener.sendNotification(user, message);

        verify(notificationService).send(user, message);
    }

    @Test
    void testSendNotification_ServiceNotFound() {
        UserDto user = new UserDto();
        user.setUsername("testuser");
        user.setPreference(UserDto.PreferredContact.TELEGRAM); // This service is not configured
        String message = "Hello, testuser!";

        assertThrows(NotificationServiceNotFoundException.class, () -> eventListener.sendNotification(user, message));
    }

    @Test
    void testGetUser_Success() {
        Long userId = 1L;
        String eventName = "TestEvent";
        Long eventId = 100L;
        UserDto expectedUser = new UserDto();
        expectedUser.setId(userId);

        when(feignUserServiceAdapter.fetchUserDtosViaFeign(userId, eventName, eventId))
                .thenReturn(Optional.of(expectedUser));

        UserDto actualUser = eventListener.getUser(userId, eventName, eventId);

        assertEquals(expectedUser, actualUser);
        verify(feignUserServiceAdapter).fetchUserDtosViaFeign(userId, eventName, eventId);
    }

    @Test
    void testGetUser_FetchFailure() {
        Long userId = 1L;
        String eventName = "TestEvent";
        Long eventId = 100L;

        when(feignUserServiceAdapter.fetchUserDtosViaFeign(userId, eventName, eventId))
                .thenReturn(Optional.empty());

        assertThrows(FetchViaFeignException.class, () -> eventListener.getUser(userId, eventName, eventId));
    }
}