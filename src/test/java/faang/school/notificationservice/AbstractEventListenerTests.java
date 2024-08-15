package faang.school.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.MentorshipOfferedEvent;
import faang.school.notificationservice.listeners.MentorshipRequestListener;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTests {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private List<NotificationService> notificationService;
    @Mock
    private List<MessageBuilder<MentorshipOfferedEvent>> messageBuilders;

    private MentorshipRequestListener mentorshipRequestListener;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        notificationService = List.of(mock(EmailService.class));
        messageBuilders = List.of(mock(MentorshipOfferedMessageBuilder.class));
        mentorshipRequestListener = new MentorshipRequestListener(objectMapper, userServiceClient, messageBuilders, notificationService);
    }

    @Test
    public void testGetMessageSuccess() {
        String str = initGetMessage();
        String result = mentorshipRequestListener.getMessage(new MentorshipOfferedEvent(), Locale.US);
        assertEquals(str, result);
    }

    @Test
    public void testGetMessageFailure() {
        when(messageBuilders.get(0).supportsEvent(any())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> mentorshipRequestListener.getMessage(new MentorshipOfferedEvent(), Locale.US));
    }

    @Test
    public void testSendNotificationSuccess() {
        String msg = "Test message";
        UserDto dto = initSendNotification();
        mentorshipRequestListener.sendNotification(1L, msg);
        verify(notificationService.get(0)).send(dto, msg);
    }

    @Test
    public void testSendNotificationFailure() {
        UserDto dto = new UserDto();
        String msg = "Test message";
        when(notificationService.get(0).getPreferredContact()).thenReturn(dto.getPreference());
        when(userServiceClient.getUser(1L)).thenReturn(dto);
        when(notificationService.get(0).getPreferredContact()).thenCallRealMethod();
        assertThrows(IllegalArgumentException.class, () -> mentorshipRequestListener.sendNotification(1L, msg));
    }

    @Test
    public void testConstructEventSuccess() {
        MentorshipOfferedEvent event = new MentorshipOfferedEvent();
        event.setAuthorId(1);
        event.setMentorId(2);
        event.setRequestId(1);
        String json = "{\"authorId\" : 1 , \"mentorId\" : 2, \"requestId\" : 1}";
        byte[] msg = json.getBytes();
        MentorshipOfferedEvent parsedEvent = mentorshipRequestListener.constructEvent(msg, MentorshipOfferedEvent.class);
        assertEquals(event, parsedEvent);
    }

    @Test
    public void testConstructEventFailure() {
        MentorshipOfferedEvent event = new MentorshipOfferedEvent();
        String json = "{\"authorId\" : 1 , \"mentorId\" : 2, \"requestId\" : 1}";
        byte[] msg = json.getBytes();
        assertThrows(IllegalArgumentException.class, () -> mentorshipRequestListener.constructEvent(msg, null));
    }

    @Test
    public void testSendMessageSuccess() {
        String str = initGetMessage();
        UserDto dto = initSendNotification();
        mentorshipRequestListener.sendMessage(new MentorshipOfferedEvent(), 1L, Locale.US);
        verify(notificationService.get(0)).send(dto, str);
    }

    private String initGetMessage() {
        String str = "You've got an offer for mentoring by {0}";
        when(messageBuilders.get(0).supportsEvent(any())).thenReturn(true);
        when(messageBuilders.get(0).buildMessage(any(), any())).thenReturn(str);
        return str;
    }

    private UserDto initSendNotification() {
        UserDto dto = new UserDto();
        String msg = "Test message";
        dto.setPreference(UserDto.PreferredContact.EMAIL);
        when(notificationService.get(0).getPreferredContact()).thenReturn(dto.getPreference());
        when(userServiceClient.getUser(1L)).thenReturn(dto);
        when(notificationService.get(0).getPreferredContact()).thenCallRealMethod();
        return dto;
    }
}
