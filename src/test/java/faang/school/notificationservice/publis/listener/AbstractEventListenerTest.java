package faang.school.notificationservice.publis.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.test_data.TestDataUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private List<NotificationService> notificationServices;
    @Mock
    private NotificationService notificationService;
    @Mock
    private NotificationService notificationService2;
    @Mock
    private List<MessageBuilder<Object>> messageBuilders;
    @Mock
    private MessageBuilder<Object> messageBuilder;
    @Mock
    private MessageBuilder<Object> messageBuilder2;
    private AbstractEventListener<Object> abstractEventListener;

    private UserDto userDto;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        abstractEventListener = new AbstractEventListener<>(
                objectMapper,
                userServiceClient,
                messageBuilders,
                notificationServices
        ) {
        };

        TestDataUser testDataUser = new TestDataUser();
        userDto = testDataUser.getUserDto();
        userDto2 = testDataUser.getUserDto2();
    }

    @Nested
    class PositiveTests {
        @Test
        void testMapToEvent_Success() throws Exception {
            Object event = new Object();
            String messageBody = "Test";
            Class<Object> eventType = Object.class;

            when(objectMapper.readValue(messageBody, eventType)).thenReturn(event);

            Object result = abstractEventListener.mapToEvent(messageBody, eventType);

            assertEquals(event, result);
        }

        @Test
        void testGetMessage_Success() {
            Object event = new Object();
            String expectedMessage = "Test";
            Locale locale = Locale.ENGLISH;

            when(messageBuilder.getInstance()).thenReturn((Class) event.getClass());
            when(messageBuilder.buildMessage(event, locale)).thenReturn(expectedMessage);
            when(messageBuilders.stream()).thenReturn(Stream.of(messageBuilder));

            String result = abstractEventListener.getMessage(event, locale);

            assertEquals(expectedMessage, result);
            verify(messageBuilder, atLeastOnce()).buildMessage(event, locale);
        }

        @Test
        public void testSendNotification_Success() {
            String expectedMessage = "Test";

            when(userServiceClient.getUser(userDto.getId())).thenReturn(userDto);
            when(notificationServices.stream()).thenReturn(Stream.of(notificationService));
            when(notificationService.getPreferredContact()).thenReturn(userDto.getPreference());

            abstractEventListener.sendNotification(userDto.getId(), expectedMessage);

            verify(notificationService, atLeastOnce()).send(userDto, expectedMessage);
        }
    }

    @Nested
    class NegativeTest {
        @Test
        void testMapToEvent_invalidParse_throwsRuntimeException() throws JsonProcessingException {
            String messageBody = "Test";
            Class<Object> eventType = Object.class;

            when(objectMapper.readValue(messageBody, eventType)).thenThrow(JsonProcessingException.class);

            assertThrows(RuntimeException.class, () -> abstractEventListener.mapToEvent(messageBody, eventType));
        }

        @Test
        public void testGetMessage_SeveralMatchingType_throwDataValidationException() {
            Object event = new Object();
            Locale locale = Locale.getDefault();

            when(messageBuilder.getInstance()).thenReturn((Class) event.getClass());
            when(messageBuilder2.getInstance()).thenReturn((Class) event.getClass());
            when(messageBuilders.stream()).thenReturn(Stream.of(messageBuilder, messageBuilder2));

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> abstractEventListener.getMessage(event, locale)
            );

            assertEquals("Several matched event types for: " + event.getClass(), exception.getMessage());
        }

        @Test
        public void testGetMessage_noMatchingType_throwDataValidationException() {
            Object event = new Object();
            Locale locale = Locale.getDefault();

            when(messageBuilders.stream()).thenReturn(Stream.empty());

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> abstractEventListener.getMessage(event, locale)
            );

            assertEquals("No matched event type for: " + event.getClass(), exception.getMessage());
        }

        @Test
        public void testSendNotification_severalMatchingPreference_throwDataValidationException() {
            String expectedMessage = "Test";

            when(userServiceClient.getUser(userDto.getId())).thenReturn(userDto);
            when(notificationServices.stream()).thenReturn(Stream.of(notificationService, notificationService2));
            when(notificationService.getPreferredContact()).thenReturn(userDto.getPreference());
            when(notificationService2.getPreferredContact()).thenReturn(userDto2.getPreference());

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> abstractEventListener.sendNotification(userDto.getId(), expectedMessage)
            );

            assertEquals("Several matched preference contact for: " + userDto.getPreference(), exception.getMessage());
        }

        @Test
        public void testSendNotification_noMatchingPreference_throwDataValidationException() {
            String expectedMessage = "Test";

            when(userServiceClient.getUser(userDto.getId())).thenReturn(userDto);
            when(notificationServices.stream()).thenReturn(Stream.empty());

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> abstractEventListener.sendNotification(userDto.getId(), expectedMessage)
            );

            assertEquals("No matched preference contact for: " + userDto.getPreference(), exception.getMessage());
        }
    }
}
