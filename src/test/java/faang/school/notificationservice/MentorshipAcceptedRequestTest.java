package faang.school.notificationservice;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedRequestEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MentorshipAcceptedRequestMessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MentorshipAcceptedRequestTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipAcceptedRequestMessageBuilder messageBuilder;


    @Test
    void positiveMessageBuild() {
        long id = 1L;
        long requesterId = 123L;
        long mentorId = 456L;
        String mentorUsername = "john doe";
        Locale locale = Locale.UK;
        MentorshipAcceptedRequestEvent event =
                new MentorshipAcceptedRequestEvent(id, requesterId, mentorId, LocalDateTime.now());
        UserDto mentor = UserDto.builder().username(mentorUsername).build();

        when(userServiceClient.getUser(mentorId)).thenReturn(mentor);
        when(messageSource.getMessage(
                eq("mentorship.accepted.notification"),
                any(Object[].class),
                eq(locale)
        )).thenReturn("Mentorship request #1 accepted by john doe");


        String result = messageBuilder.buildMessage(event, locale);


        verify(messageSource).getMessage(
                eq("mentorship.accepted.notification"),
                any(Object[].class),
                eq(locale)
        );

        assertEquals("Mentorship request #1 accepted by john doe", result);
    }

    @Test
    void positiveCorrectClass() {
        MentorshipAcceptedRequestMessageBuilder builder =
                new MentorshipAcceptedRequestMessageBuilder(null, null);
        assertEquals(MentorshipAcceptedRequestEvent.class, builder.getInstance());
    }

}
