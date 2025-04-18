package faang.school.notificationservice.event_listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.AnotherTestEvent;
import faang.school.notificationservice.event.TestEvent;
import faang.school.notificationservice.exception.BuildMessageFailedException;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.exception.UserNotFoundException;
import faang.school.notificationservice.messaging.HelloWorldEventMessageBuilder;
import faang.school.notificationservice.messaging.JsonTestEventMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.TestEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private final NotificationService smsNotificationService = Mockito.mock(NotificationService.class);
    private final NotificationService emailNotificationService = Mockito.mock(NotificationService.class);
    private final NotificationService anotherEmailNotificationService = Mockito.mock(NotificationService.class);

    @BeforeEach
    public void setUp() {
        when(smsNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.PHONE);
        when(emailNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(anotherEmailNotificationService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);

        notificationServices = new ArrayList<>();
        notificationServices.add(smsNotificationService);
        notificationServices.add(emailNotificationService);
        notificationServices.add(anotherEmailNotificationService);
    }

    @Test
    public void shouldPostConstruct_throws_whenSeveralMessageBuildersForSingleEventType() {
        messageBuilders = new ArrayList<>();
        messageBuilders.add(new TestEventMessageBuilder());
        messageBuilders.add(new HelloWorldEventMessageBuilder());
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertThrows(IllegalStateException.class, abstractEventListener::postConstruct);
    }

    @Test
    public void shouldPostConstruct_success_whenNoMessageBuildersAreRegistered() {
        messageBuilders = List.of();
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertDoesNotThrow(abstractEventListener::postConstruct);
    }

    @Test
    public void shouldPostConstruct_success_whenAllMessageBuildersProcessDifferentEventTypes() {
        messageBuilders = new ArrayList<>();
        messageBuilders.add(new TestEventMessageBuilder());
        messageBuilders.add(new JsonTestEventMessageBuilder(objectMapper));
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertDoesNotThrow(abstractEventListener::postConstruct);
    }

    @Test
    public void shouldGetMessage_success_whenEventObjectIsPassed() throws Exception {
        // Arrange
        var messageBuilder = Mockito.mock(TestEventMessageBuilder.class);
        messageBuilders = new ArrayList<>();
        messageBuilders.add(messageBuilder);
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
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
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
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
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
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
    public void shouldGetMessage_throws_whenBuilderIsNotFound() {
        // Подготовка данных
        Class<?> otherClass = String.class;
        messageBuilders = new ArrayList<>();
        messageBuilders.add(new TestEventMessageBuilder());
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertThrows(BuildMessageFailedException.class,
                () -> abstractEventListener.getMessage(otherClass, TEST_LOCALE));
    }

    @Test
    public void shouldGetMessage_throws_whenInvalidJsonIsPassed() throws Exception {
        var invalidJson = "{invalid json}";
        messageBuilders = new ArrayList<>();
        messageBuilders.add(new TestEventMessageBuilder());
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertThrows(BuildMessageFailedException.class,
                () -> abstractEventListener.getMessage(TestEvent.class, TEST_LOCALE, invalidJson));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    public void shouldSendNotification_throws_whenMessageIsInvalid(String message) {
        messageBuilders = List.of();
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        assertThrows(DataValidationException.class,
                () -> abstractEventListener.sendNotification(faker.number().randomNumber(), message));
        verifyNoInteractions(userServiceClient);
        verifyNoInteractions(smsNotificationService);
        verifyNoInteractions(emailNotificationService);
        verifyNoInteractions(anotherEmailNotificationService);
    }

    @Test
    public void shouldSendNotification_throws_whenUserIsNotFound() {
        // Arrange
        messageBuilders = List.of();
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        var userId = faker.number().randomNumber();
        when(userServiceClient.getUser(userId)).thenThrow(new RuntimeException("Service error"));

        // Act + Assert
        assertThrows(UserNotFoundException.class,
                () -> abstractEventListener.sendNotification(userId, "Test message"));
        verify(userServiceClient, times(1)).getUser(userId);
        verifyNoInteractions(smsNotificationService);
        verifyNoInteractions(emailNotificationService);
        verifyNoInteractions(anotherEmailNotificationService);
    }

    @Test
    public void shouldSendNotification_success_whenNotificationServiceIsFound() {
        // Arrange
        var userId = faker.number().randomNumber();
        messageBuilders = List.of();
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        var testUser = UserDto.builder()
                .id(userId)
                .phone(faker.phoneNumber().phoneNumber())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .preference(UserDto.PreferredContact.EMAIL)
                .build();
        when(userServiceClient.getUser(userId)).thenReturn(testUser);

        var message = "Test message";

        // Act
        var result = abstractEventListener.sendNotification(userId, message);

        // Assert
        assertDoesNotThrow(result::join);
        verify(userServiceClient, times(1)).getUser(userId);
        verify(emailNotificationService, times(1)).send(testUser, message);
        verify(smsNotificationService, times(0)).send(any(), anyString());
        verify(anotherEmailNotificationService).send(testUser, message);
    }

    @Test
    public void shouldSendNotification_success_whenNoMatchingService() {
        // Arrange
        var userId = faker.number().randomNumber();
        messageBuilders = List.of();
        var abstractEventListener = new AbstractEventListener(objectMapper, userServiceClient, notificationServices,
                messageBuilders);

        var testUser = UserDto.builder()
                .id(userId)
                .phone(faker.phoneNumber().phoneNumber())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();
        when(userServiceClient.getUser(userId)).thenReturn(testUser);

        // Act
        var result = abstractEventListener.sendNotification(userId, "Test message");

        // Assert
        assertDoesNotThrow(result::join);
        verify(userServiceClient, times(1)).getUser(userId);
        verify(emailNotificationService, times(0)).send(any(), anyString());
        verify(smsNotificationService, times(0)).send(any(), anyString());
        verify(anotherEmailNotificationService, times(0)).send(any(), anyString());
    }
}