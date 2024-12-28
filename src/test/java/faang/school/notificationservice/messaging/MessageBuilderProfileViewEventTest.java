package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageBuilderProfileViewEventTest {

    @InjectMocks
    private MessageBuilderProfileViewEvent messageBuilderProfileViewEvent;

    @Mock
    private MessageSource messageSource;


    @Test
    void testbuildMessage() {
        ProfileViewEvent event = new ProfileViewEvent();
        event.setAuthorId(1l);
        event.setViewerName("Name");
        UserDto userDto = new UserDto();
        userDto.setUsername("123");

        String followerKey ="222";
        when(messageSource.getMessage(any(),any(),any())).thenReturn(followerKey);
        String result = messageBuilderProfileViewEvent.buildMessage(event,Locale.getDefault());
        assertEquals(result,followerKey);
    }
    @Test
    void testGetInstance() {
        Class<?> actual = messageBuilderProfileViewEvent.getInstance();
        assertEquals(actual, ProfileViewEvent.class);
    }
}