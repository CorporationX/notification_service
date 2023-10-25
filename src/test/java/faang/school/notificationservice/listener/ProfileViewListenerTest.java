package faang.school.notificationservice.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.dto.event.ProfileViewEventDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.ProfileViewMessageBuilder;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.sms_sending.SmsService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileViewListenerTest {
    @Mock
    ObjectMapper objectMapper;
    @Mock
    ProfileViewMessageBuilder messageBuilder;
    @Mock
    UserServiceClient userServiceClient;
    @Mock
    EmailService emailService;
    @Mock
    SmsService smsService;
    @InjectMocks
    ProfileViewListener profileViewListener;


    @Test
    void onMessageTest() throws IOException {
        ProfileViewEventDto event = ProfileViewEventDto.builder().profileOwnerId(1L).viewerId(2L).build();
        UserDto user = UserDto.builder().username("Alex").preference(UserDto.PreferredContact.EMAIL).email("123@123").build();

        Message message = mock(Message.class);
        byte[] body = new byte[0];

        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, ProfileViewEventDto.class)).thenReturn(event);
        when(messageBuilder.buildMessage(event, "eng")).thenReturn("message text");
        when(userServiceClient.getUser(event.getProfileOwnerId())).thenReturn(user);

        profileViewListener.onMessage(message, null);
        verify(objectMapper).readValue(message.getBody(), EventStartDto.class);
        verify(userServiceClient).getUser(event.getProfileOwnerId());
        verify(emailService).sendMail(user.getEmail(), "Profile View Notification", "message text");
    }
}
