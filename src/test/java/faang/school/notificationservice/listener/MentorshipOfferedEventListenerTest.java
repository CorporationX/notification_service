package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.mentorship.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.listeners.MentorshipOfferedEventListener;
import faang.school.notificationservice.messaging.message_builder.MentorshipOfferedEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipOfferedEventListenerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Captor
    private ArgumentCaptor<UserDto> userDtoArgumentCaptor;

    @Mock
    private NotificationService notificationService;
    @Mock
    private UserService userService;
    @Mock
    private MentorshipOfferedEventMessageBuilder mentorshipOfferedEventMessageBuilder;

    private MentorshipOfferedEventListener mentorshipOfferedEventListener;

    @BeforeEach
    void setup() {
        mentorshipOfferedEventListener = new MentorshipOfferedEventListener(objectMapper,
                List.of(notificationService), userService, mentorshipOfferedEventMessageBuilder);

        mentorshipOfferedEventListener.init();
    }

    @Test
    void testOnMessage(){
        MentorshipOfferedEvent mentorshipOfferedEvent = MentorshipOfferedEvent.builder()
                .mentorshipRequestId(3L)
                .mentorId(1L)
                .menteeId(2L)
                .build();

        UserDto userDto = UserDto.builder()
                .id(mentorshipOfferedEvent.mentorId())
                .preference(UserDto.PreferredContact.SMS)
                .locale(Locale.CANADA)
                .build();

        String messageText = "test text";

        when(mentorshipOfferedEventMessageBuilder.buildMessage(Mockito.any(MentorshipOfferedEvent.class),
                Mockito.any(Locale.class))).thenReturn(messageText);
        when(userService.getUser(userDto.getId())).thenReturn(userDto);
        when(notificationService.getPreferredContact()).thenReturn(userDto.getPreference());

        mentorshipOfferedEventListener.onMessage(mentorshipOfferedEvent);

        verify(notificationService).send(userDtoArgumentCaptor.capture(), Mockito.eq(messageText));

        UserDto capturedUserDto = userDtoArgumentCaptor.getValue();

        Assertions.assertEquals(userDto.getId(), capturedUserDto.getId());
    }
}