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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestListenerTest {
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceClient userServiceClient;

    private List<NotificationService> notificationServices;
    private List<MessageBuilder<MentorshipOfferedEvent>> messageBuilders;
    @InjectMocks
    private MentorshipRequestListener mentorshipRequestListener;
    private MentorshipOfferedEvent event;
    private String str;
    private Message message;
    private byte[] messageBytes;

    @BeforeEach
    public void setUp() {
        event = new MentorshipOfferedEvent(1L, 2L, 3L);
        str = "{\"authorId\": 1, \"mentorId\" : 2, \"requestId\" : 3}";
        messageBytes = str.getBytes();
        message = mock(Message.class);
        messageBuilders = List.of(mock(MentorshipOfferedMessageBuilder.class));
        notificationServices = List.of(mock(EmailService.class));
        mentorshipRequestListener = new MentorshipRequestListener(objectMapper,userServiceClient,messageBuilders,notificationServices);
    }

    @Test
    public void testConstructEventSuccess() {
        MentorshipOfferedEvent result = mentorshipRequestListener.constructEvent(messageBytes, MentorshipOfferedEvent.class);
        assertEquals(event, result);
    }

    @Test
    public void testConstructEventFailure() {
        when(mentorshipRequestListener.constructEvent(messageBytes, MentorshipOfferedEvent.class)).thenThrow(new IOException());
        assertThrows(RuntimeException.class, () -> mentorshipRequestListener.constructEvent(messageBytes, MentorshipOfferedEvent.class));
    }

    @Test
    public void testSendMessageSuccess() throws IOException {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPreference(UserDto.PreferredContact.EMAIL);
        MentorshipOfferedEvent result = mentorshipRequestListener.constructEvent(messageBytes, MentorshipOfferedEvent.class);
        when(messageBuilders.get(0).supportsEvent(any())).thenReturn(true);
        when(messageBuilders.get(0).buildMessage(result,Locale.US)).thenReturn("Message");
        when(userServiceClient.getUser(2L)).thenReturn(userDto);
        when(notificationServices.get(0).getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL).thenCallRealMethod();
        mentorshipRequestListener.sendMessage(result, result.getMentorId(),Locale.US);
        assertEquals("Message",messageBuilders.get(0).buildMessage(result,Locale.US));
    }
}
