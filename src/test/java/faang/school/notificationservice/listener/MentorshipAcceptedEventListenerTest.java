package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MentorshipAcceptedMessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedEventListenerTest {

    private static final long MENTEE_ID = 1L;
    private static final long MENTOR_ID = 2L;
    private static final long REQUEST_ID = 3L;
    private static final String MENTEE_USER_NAME = "Mentee User Name";
    private static final String MENTOR_USER_NAME = "Mentor User Name";
    private static final String EMAIL = "test@gmail.com";
    private static final String BUILD_MESSAGE = MENTOR_USER_NAME + " accepted your request " + REQUEST_ID + " for mentorship";

    private MentorshipAcceptedEventListener mentorshipAcceptedEventListener;
    private ObjectMapper objectMapper;
    private UserServiceClient userServiceClient;
    private MentorshipAcceptedEvent messageEvent;
    private EmailService emailService;
    private MentorshipAcceptedMessageBuilder builder;
    private UserDto userMenteeDto;

    @BeforeEach
    public void init() {
        messageEvent = MentorshipAcceptedEvent.builder()
                .actorId(MENTEE_ID)
                .receiverId(MENTOR_ID)
                .requestId(REQUEST_ID)
                .build();

        userMenteeDto = UserDto.builder()
                .id(MENTEE_ID)
                .username(MENTEE_USER_NAME)
                .email(EMAIL)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        objectMapper = Mockito.mock(ObjectMapper.class);
        userServiceClient = mock(UserServiceClient.class);
        emailService = Mockito.mock(EmailService.class);
        builder = Mockito.mock(MentorshipAcceptedMessageBuilder.class);

        List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders = mock(List.class);
        List<NotificationService> notificationServices = mock(List.class);

        mentorshipAcceptedEventListener = new MentorshipAcceptedEventListener(objectMapper, userServiceClient,
                notificationServices, messageBuilders);

        doReturn(MentorshipAcceptedEvent.class).when(builder).supportsEventType();
        when(builder.buildMessage(messageEvent, Locale.ENGLISH)).thenReturn(BUILD_MESSAGE);
        when(messageBuilders.stream()).thenAnswer(invocation -> Stream.of(builder));
        when(emailService.getPreferredContact()).thenReturn(UserDto.PreferredContact.EMAIL);
        when(notificationServices.stream()).thenAnswer(invocation -> Stream.of(emailService));
    }

    @Test
    public void whenEventHandle() throws IOException {
        Message message = mock(Message.class);
        byte[] pattern = new byte[]{};
        String body = "{\"actorId\":1,\"receiverId\":2,\"requestId\":3}";
        String channel = "mentorship_accepted_channel";

        when(message.getBody()).thenReturn(body.getBytes());
        when(message.getChannel()).thenReturn(channel.getBytes());
        when(objectMapper.readValue(body.getBytes(), MentorshipAcceptedEvent.class)).thenReturn(messageEvent);
        when(userServiceClient.getUser(MENTEE_ID)).thenReturn(userMenteeDto);

        mentorshipAcceptedEventListener.onMessage(message, pattern);

        verify(emailService).send(userMenteeDto, BUILD_MESSAGE);
    }
}