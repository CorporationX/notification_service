package faang.school.notificationservice.event_listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.AnotherTestEvent;
import faang.school.notificationservice.event.TestEvent;
import faang.school.notificationservice.exception.BuildMessageFailedException;
import faang.school.notificationservice.messaging.HelloWorldEventMessageBuilder;
import faang.school.notificationservice.messaging.JsonTestEventMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.TestEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {

    public static final Locale TEST_LOCALE = Locale.US;

    private final Faker faker = new Faker();

    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;

    private List<NotificationService> notificationServices;
    private List<MessageBuilder<?>> messageBuilders;

    private AbstractEventListener abstractEventListener;

    @Test
    public void shouldPostConstruct_throws_whenSeveralMessageBuildersForSingleEventType() {
        notificationServices = List.of();
        messageBuilders = new ArrayList<>();
        messageBuilders.add(new TestEventMessageBuilder());
        messageBuilders.add(new HelloWorldEventMessageBuilder());
        abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertThrows(IllegalStateException.class, () -> abstractEventListener.postConstruct());
    }

    @Test
    public void shouldPostConstruct_success_whenNoMessageBuildersAreRegistered() {
        notificationServices = List.of();
        messageBuilders = List.of();
        abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertDoesNotThrow(() -> abstractEventListener.postConstruct());
    }

    @Test
    public void shouldPostConstruct_success_whenAllMessageBuildersProcessDifferentEventTypes() {
        notificationServices = List.of();
        messageBuilders = new ArrayList<>();
        messageBuilders.add(new TestEventMessageBuilder());
        messageBuilders.add(new JsonTestEventMessageBuilder(objectMapper));
        abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertDoesNotThrow(() -> abstractEventListener.postConstruct());
    }

    @Test
    public void shouldGetMessage_success_whenEventObjectIsPassed() throws Exception {
        // Arrange
        var messageBuilder = Mockito.mock(TestEventMessageBuilder.class);
        messageBuilders = new ArrayList<>();
        messageBuilders.add(messageBuilder);
        notificationServices = List.of();
        abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        when(messageBuilder.getInstance()).thenAnswer(invocationOnMock -> TestEvent.class);

        var testEvent = new TestEvent(faker.name().fullName());
        var expectedMessage = "Result message";
        when(messageBuilder.buildMessage(eq(testEvent), eq(TEST_LOCALE))).thenReturn(expectedMessage);

        // Act
        var result = abstractEventListener.getMessage(TestEvent.class, TEST_LOCALE, testEvent);

        // Assert
        assertEquals(expectedMessage, result);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void shouldGetMessage_success_whenArgsForEventConstructorArePassed() throws Exception {
        // Arrange
        var messageBuilder = Mockito.mock(JsonTestEventMessageBuilder.class);
        messageBuilders = new ArrayList<>();
        messageBuilders.add(messageBuilder);
        notificationServices = List.of();
        abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        when(messageBuilder.getInstance()).thenAnswer(invocationOnMock -> AnotherTestEvent.class);

        var title = faker.name().title();
        var message = faker.name().name();
        var testEvent = new AnotherTestEvent(title, message);
        var expectedMessage = "Result message";
        when(messageBuilder.buildMessage(eq(testEvent), eq(TEST_LOCALE))).thenReturn(expectedMessage);

        // Act
        var result = abstractEventListener.getMessage(AnotherTestEvent.class, TEST_LOCALE, title, message);

        // Assert
        assertEquals(expectedMessage, result);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void shouldGetMessage_success_whenJsonIsPassed() throws Exception {
        // Arrange
        var messageBuilder = Mockito.mock(TestEventMessageBuilder.class);
        messageBuilders = new ArrayList<>();
        messageBuilders.add(messageBuilder);
        notificationServices = List.of();
        abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        when(messageBuilder.getInstance()).thenAnswer(invocationOnMock -> TestEvent.class);

        var text = "test";
        var json = "{\"text\":\"%s\"}".formatted(text);
        var testEvent = new TestEvent(text);
        var expectedMessage = "JSON message";
        when(messageBuilder.buildMessage(eq(testEvent), eq(TEST_LOCALE))).thenReturn(expectedMessage);

        // Act
        var result = abstractEventListener.getMessage(TestEvent.class, TEST_LOCALE, json);

        // Assert
        assertEquals(expectedMessage, result);
        verify(objectMapper, times(1)).readValue(eq(json), eq(TestEvent.class));
    }

    @Test
    void shouldGetMessage_throws_whenBuilderIsNotFound() {
        // Подготовка данных
        Class<?> otherClass = String.class;
        notificationServices = List.of();
        messageBuilders = new ArrayList<>();
        messageBuilders.add(new TestEventMessageBuilder());
        abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertThrows(BuildMessageFailedException.class,
                () -> abstractEventListener.getMessage(otherClass, TEST_LOCALE));
    }

    @Test
    void shouldGetMessage_throws_whenInvalidJsonIsPassed() throws Exception {
        var invalidJson = "{invalid json}";
        notificationServices = List.of();
        messageBuilders = new ArrayList<>();
        messageBuilders.add(new TestEventMessageBuilder());
        abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertThrows(BuildMessageFailedException.class,
                () -> abstractEventListener.getMessage(TestEvent.class, TEST_LOCALE, invalidJson));
    }
}