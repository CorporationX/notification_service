package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.MentorshipEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MentorshipEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipEventMessageBuilder mentorshipEventMessageBuilder;

    @Test
    public void buildMessageTest() {
        UserDto userDto = UserDto.builder()
                .username("Name")
                .locale(Locale.CHINA)
                .build();
        LocalDateTime now = LocalDateTime.now();
        MentorshipEventDto eventDto = MentorshipEventDto.builder()
                .requesterId(1L)
                .receiverId(2L)
                .createdAt(now)
                .build();
        mentorshipEventMessageBuilder.buildMessage(eventDto, userDto);
        verify(messageSource).getMessage("mentorship.new", new Object[]{1L, now}, userDto.getLocale());
    }
}
