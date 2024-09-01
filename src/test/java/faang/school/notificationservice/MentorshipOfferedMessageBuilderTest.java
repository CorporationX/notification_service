package faang.school.notificationservice;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.events.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class MentorshipOfferedMessageBuilderTest {
    @Autowired
    private MentorshipOfferedMessageBuilder mentorshipOfferedMessageBuilder;
    @MockBean
    private UserServiceClient userServiceClient;

    @Test
    public void testBuildMessageUS() {
        String str = "You have got an offer for mentoring by Jack Nicholson";
        MentorshipOfferedEvent event = new MentorshipOfferedEvent();
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("Jack Nicholson");
        event.setAuthorId(1L);

        when(userServiceClient.getUser(event.getAuthorId())).thenReturn(dto);
        String result = mentorshipOfferedMessageBuilder.buildMessage(event, Locale.US);
        assertEquals(str, result);
    }

    @Test
    public void testBuildMessageFR() {
        String str = "Vous avez recu une offre de mentorat de la part de Jack Nicholson";
        MentorshipOfferedEvent event = new MentorshipOfferedEvent();
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("Jack Nicholson");
        event.setAuthorId(1L);

        when(userServiceClient.getUser(event.getAuthorId())).thenReturn(dto);
        String result = mentorshipOfferedMessageBuilder.buildMessage(event, Locale.FRANCE);
        assertEquals(str, result);
    }

    @Test
    public void testBuildMessageRU() {
        String str = "Вы получили предложение на менторство от Jack Nicholson";
        MentorshipOfferedEvent event = new MentorshipOfferedEvent();
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("Jack Nicholson");
        event.setAuthorId(1L);

        when(userServiceClient.getUser(event.getAuthorId())).thenReturn(dto);
        String result = mentorshipOfferedMessageBuilder.buildMessage(event, new Locale("ru"));
        assertEquals(str, result);
    }

    @Test
    public void testSupportsEventSuccess(){
        Class<?> result = mentorshipOfferedMessageBuilder.supportsEvent();
        assertEquals(MentorshipOfferedEvent.class, result);
    }
}
