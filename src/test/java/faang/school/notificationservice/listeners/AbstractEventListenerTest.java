package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MappingException;
import faang.school.notificationservice.exception.MessageBuilderNotFoundException;
import faang.school.notificationservice.exception.ServiceNotFoundException;
import faang.school.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageBuilderForTest messageBuilder;

    private EventListenerForTest eventListener;

    @BeforeEach
    void setUp() {
        eventListener = new EventListenerForTest(objectMapper, userServiceClient,
                List.of(notificationService), List.of(messageBuilder));
    }

    @Test
    void testHandleEventSuccessfully() throws IOException {
        String eventName = "Test Event";
        long invitedId = 1L;
        String messageContent = "{\"invitedId\": 1, \"name\": \"Test Event\"}";
        EventForTest expectedEvent = new EventForTest(invitedId, eventName);
        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(messageContent.getBytes(StandardCharsets.UTF_8));
        when(objectMapper.readValue(redisMessage.getBody(), EventForTest.class)).thenReturn(expectedEvent);

        eventListener.handleEvent(redisMessage, EventForTest.class, event ->
                assertEquals(expectedEvent.getName(), event.getName())
        );

        verify(objectMapper, times(1)).readValue(redisMessage.getBody(), EventForTest.class);
    }

    @Test
    void testHandleEvent_whenThrowsJsonParseException() throws IOException {
        byte[] wrongJSON = "invitedId: 1, name: Test Event".getBytes(StandardCharsets.UTF_8);
        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(wrongJSON);
        when(objectMapper.readValue(wrongJSON, EventForTest.class)).thenThrow(JsonParseException.class);

        assertThrows(MappingException.class, () ->
                eventListener.handleEvent(redisMessage, EventForTest.class, event -> {
                })
        );

        verify(objectMapper, times(1)).readValue(redisMessage.getBody(), EventForTest.class);
    }

    @Test
    void testHandleEvent_whenThrowsJsonMappingException() throws IOException {
        byte[] wrongJSON = "{\"invitedId\": 1, \"name\": \"Test Event\", \"optionalField\":\"Optional Field\"}"
                .getBytes(StandardCharsets.UTF_8);
        Message redisMessage = mock(Message.class);
        when(redisMessage.getBody()).thenReturn(wrongJSON);
        when(objectMapper.readValue(wrongJSON, EventForTest.class)).thenThrow(JsonMappingException.class);

        assertThrows(MappingException.class, () ->
                eventListener.handleEvent(redisMessage, EventForTest.class, event -> {
                })
        );

        verify(objectMapper, times(1)).readValue(redisMessage.getBody(), EventForTest.class);
    }

    @Test
    void testGetMessageSuccessfully() {
        Locale locale = Locale.ENGLISH;
        String eventName = "Test Event";
        String message = "Message";
        long invitedId = 1L;
        EventForTest event = new EventForTest(invitedId, eventName);
        when(messageBuilder.supportsEventType()).thenAnswer(invocation -> EventForTest.class);
        when(messageBuilder.buildMessage(event, locale)).thenReturn(message);

        String resultMessage = eventListener.getMessage(event, locale);
        verify(messageBuilder, times(1)).supportsEventType();
        verify(messageBuilder, times(1)).buildMessage(event, locale);
        assertEquals(message, resultMessage);
    }

    @Test
    void testGetMessage_whenThrowsMessageBuilderNotFoundException() {
        Locale locale = Locale.ENGLISH;
        String eventName = "Test Event";
        long invitedId = 1L;
        EventForTest event = new EventForTest(invitedId, eventName);

        when(messageBuilder.supportsEventType()).thenReturn(null);

        assertThrows(MessageBuilderNotFoundException.class, () ->
                eventListener.getMessage(event, locale));
    }

    @Test
    void testSendNotificationSuccessfully() {
        long invitedId = 1L;
        String message = "Message";
        UserDto.PreferredContact prefContact = UserDto.PreferredContact.EMAIL;
        UserDto userDto = UserDto.builder()
                .preference(prefContact)
                .id(invitedId)
                .build();

        when(userServiceClient.getUser(invitedId)).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(prefContact);

        eventListener.sendNotification(invitedId, message);

        verify(userServiceClient, times(1)).getUser(invitedId);
        verify(notificationService, times(1)).getPreferredContact();
        verify(notificationService, times(1)).send(userDto, message);
    }

    @Test
    void testSendNotification_whenThrowsServiceNotFoundException() {
        long invitedId = 1L;
        String message = "Message";
        UserDto.PreferredContact preferredContact = any();
        UserDto user = UserDto.builder()
                .preference(preferredContact)
                .id(invitedId)
                .build();

        when(userServiceClient.getUser(invitedId)).thenReturn(user);
        doThrow(new ServiceNotFoundException("No notification service found for the user`s id: " + invitedId
                + "preferred method: " + user.getPreference()))
                .when(notificationService).getPreferredContact();

        assertThrows(ServiceNotFoundException.class,
                () -> eventListener.sendNotification(invitedId, message));
    }
}