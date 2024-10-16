package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;

import faang.school.notificationservice.service.telegram.TelegramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MentorshipOfferedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MentorshipOfferedMessageBuilder messageBuilder;

    @Mock
    private Message message;

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private MentorshipOfferedEventListener mentorshipOfferedEventListener;

    private MentorshipOfferedEvent mentorshipOfferedEvent;
    private UserDto userDto;
    private Locale locale;
    private String jsonMentorshipOfferedEvent;

    @BeforeEach
    void setup(){
        jsonMentorshipOfferedEvent = "{ \"requestId\": 1, \"requesterId\": 1, \"receiverId\": 2 }";

        locale = Locale.getDefault();

        userDto = UserDto.builder()
                .id(1L)
                .preference(UserDto.PreferredContact.TELEGRAM)
                .build();

        mentorshipOfferedEventListener = new MentorshipOfferedEventListener(
                objectMapper,
                userServiceClient,
                messageBuilder,
                List.of(telegramService)
        );

        mentorshipOfferedEvent = MentorshipOfferedEvent.builder()
                .requestId(1L)
                .requesterId(1L)
                .receiverId(2L)
                .build();
    }

    @Test
    void testOnMessageOk() throws IOException {
        when(message.getBody()).thenReturn(jsonMentorshipOfferedEvent.getBytes());
        when(objectMapper.readValue(jsonMentorshipOfferedEvent.getBytes(), MentorshipOfferedEvent.class)).thenReturn(mentorshipOfferedEvent);
        when(messageBuilder.buildMessage(mentorshipOfferedEvent, locale)).thenReturn("babushka");
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        when(telegramService.getPreferredContact()).thenReturn(UserDto.PreferredContact.TELEGRAM);

        mentorshipOfferedEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(message.getBody(), MentorshipOfferedEvent.class);
        verify(messageBuilder).buildMessage(any(), any());
        verify(userServiceClient).getUser(anyLong());
    }
}
