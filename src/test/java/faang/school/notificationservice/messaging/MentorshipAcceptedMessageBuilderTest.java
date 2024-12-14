package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.MentorshipAcceptedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipAcceptedMessageBuilderTest {

    @InjectMocks
    private MentorshipAcceptedMessageBuilder builder;

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @Test
    public void testGetInstance() {
        Class<?> clazz = builder.getInstance();
        assertEquals(MentorshipAcceptedEvent.class, clazz);
    }

    @Test
    public void testBuildMessage() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setLocale(new Locale("ru", "RU"));

        MentorshipAcceptedEvent event = new MentorshipAcceptedEvent();
        event.setReceiverUserId(1L);

        String text = "text";

        when(userServiceClient.getUser(user.getId())).thenReturn(user);
        when(messageSource.getMessage(any(), any(), any())).thenReturn(text);

        String result = builder.buildMessage(event, user.getLocale());

        assertEquals(text, result);
    }
}
