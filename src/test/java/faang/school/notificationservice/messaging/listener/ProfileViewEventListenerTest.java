package faang.school.notificationservice.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.messaging.message_builder.ProfileViewMessageBuilder;
import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.redis.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.sms.SmsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(value = {MockitoExtension.class})
public class ProfileViewEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageSource messageSource;

    @Mock
    private Message message;

    @Mock
    private ProfileViewMessageBuilder builder;

    @Mock
    private SmsService smsService;

    @InjectMocks
    private ProfileViewEventListener listener;

    @Test
    public void testOnMessage() throws IOException {
        ProfileViewEvent event = ProfileViewEvent.builder()
                .userId(1L)
                .profileViewedId(2L)
                .date(LocalDateTime.now())
                .build();
        UserDto user = UserDto.builder()
                .preference(UserDto.PreferredContact.SMS)
                .build();
        builder = new ProfileViewMessageBuilder(userServiceClient, messageSource);
        Locale locale = Locale.ENGLISH;
        String notificationMsg = "notification message";

        listener = new ProfileViewEventListener(objectMapper, userServiceClient, builder, List.of(smsService));


        when(objectMapper.readValue(eq(message.getBody()), eq(ProfileViewEvent.class))).thenReturn(event);
        when(userServiceClient.getUser(event.getProfileViewedId())).thenReturn(user);
        when(builder.buildMessage(event, locale)).thenReturn(notificationMsg);
        when(userServiceClient.getUser(event.getProfileViewedId())).thenReturn(user);
        when(smsService.getPreferredContact()).thenReturn(UserDto.PreferredContact.SMS);

        listener.onMessage(message, null);
        verify(smsService).send(user, notificationMsg);
    }

}
