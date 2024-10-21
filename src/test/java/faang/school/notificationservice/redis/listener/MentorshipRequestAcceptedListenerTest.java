package faang.school.notificationservice.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClientMock;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MentorshipRequestAcceptedEventMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.dto.MentorshipRequestAcceptedDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.SmsNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MentorshipRequestAcceptedListenerTest {

    private UserServiceClientMock userServiceClient;
    private MessageBuilder<MentorshipRequestAcceptedDto> messageBuilder;
    private NotificationService notificationService;
    private ObjectMapper objectMapper;

    private MentorshipRequestAcceptedListener listener;

    @BeforeEach
    public void setUp() {
        userServiceClient = mock(UserServiceClientMock.class);
        messageBuilder = mock(MentorshipRequestAcceptedEventMessageBuilder.class);
        notificationService = mock(SmsNotificationService.class);
        objectMapper = new ObjectMapper();

        listener = new MentorshipRequestAcceptedListener(userServiceClient,
                Collections.singletonList(messageBuilder),
                Collections.singletonList(notificationService),
                objectMapper);
    }

    @Test
    public void testProcessEvent() {
        long actorId = 1L;
        long receiverId = 2L;
        long requestId = 123L;

        MentorshipRequestAcceptedDto event = new MentorshipRequestAcceptedDto(requestId, actorId, receiverId);

        UserDto userDto = new UserDto();
        userDto.setId(receiverId);
        userDto.setUsername("testUser");
        userDto.setLocale(Locale.ENGLISH);

        when(userServiceClient.getUser(receiverId)).thenReturn(userDto);
        when(messageBuilder.getInstance()).thenReturn((Class) MentorshipRequestAcceptedDto.class);
        when(messageBuilder.buildMessage(event, userDto.getLocale())).thenReturn("mentorship_request.accepted");

        listener.processEvent(event);

        ArgumentCaptor<UserDto> userDtoCaptor = ArgumentCaptor.forClass(UserDto.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(notificationService).send(userDtoCaptor.capture(), messageCaptor.capture());
        assertEquals(userDto, userDtoCaptor.getValue());
        assertEquals("mentorship_request.accepted", messageCaptor.getValue());
    }
}