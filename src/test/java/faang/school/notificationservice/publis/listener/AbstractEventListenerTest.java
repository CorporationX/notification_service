package faang.school.notificationservice.publis.listener;

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
import static org.mockito.ArgumentMatchers.anyString;
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
    private List<MessageBuilder<Object>> messageBuilders;
    @Mock
    private MessageBuilder<Object> messageBuilder;
    private AbstractEventListener<Object> abstractEventListener;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        abstractEventListener = new AbstractEventListener<>(
                objectMapper,
                userServiceClient,
                notificationServices,
                messageBuilders
        ) {
        };

        TestDataUser testDataUser = new TestDataUser();
        userDto = testDataUser.getUserDto();
    }

    @Nested
    class PositiveTests {
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
        public void testGetMessage_NoMatchingBuilder_throwDataValidationException() {
            Object event = new Object();
            Locale locale = Locale.ENGLISH;

            when(messageBuilders.stream()).thenReturn(Stream.empty());

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> abstractEventListener.getMessage(event, locale)
            );

            assertEquals("No matched event type or no message builder found for this event type", exception.getMessage());
        }

        @Test
        public void testSendNotification_NoMatchingPreference_throwDataValidationException() {
            when(notificationServices.stream()).thenReturn(Stream.empty());

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> abstractEventListener.sendNotification(userDto.getId(), anyString())
            );

            assertEquals("No matched preference contact", exception.getMessage());
        }
    }
}
