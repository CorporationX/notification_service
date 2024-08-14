package faang.school.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;

import faang.school.notificationservice.events.MentorshipOfferedEvent;
import faang.school.notificationservice.listeners.MentorshipRequestListener;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTests {
    private MentorshipRequestListener mentorshipEventListener;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        UserServiceClient userServiceClient = mock(UserServiceClient.class);
        List<NotificationService> notificationServices = List.of(mock(EmailService.class));
        List<MessageBuilder<MentorshipOfferedEvent>> messageBuilders = List.of(mock(MentorshipOfferedMessageBuilder.class));
        mentorshipEventListener = new MentorshipRequestListener(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Test
    public void testOnMessage() {
        String str = "You've got an offer for mentoring by {0}";
        String json = "{\"authorId\" : 1 , \"mentorId\" : 2, \"requestId\" : 1}";
        Message message = Message
        MentorshipOfferedEvent event = new MentorshipOfferedEvent();
        event.setAuthorId(1L);
        event.setMentorId(2L);
        event.setRequestId(1L);
        objectMapper.writeValueAsString(event);
        when(objectMapper.readValue(any(), MentorshipOfferedEvent.class)).thenReturn()
        when(mentorshipEventListener.constructEvent(any(), MentorshipOfferedEvent.class)).thenReturn(event);
        String msg = mentorshipEventListener.sendMessage(event, event.getMentorId(), Locale.US);
    }

}
