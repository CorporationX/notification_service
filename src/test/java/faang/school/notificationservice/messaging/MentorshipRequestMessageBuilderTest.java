package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.mentorship.MentorshipRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipRequestMessageBuilder messageBuilder;

    @Test
    public void testBuildMessage() {
        MentorshipRequestDto mentorshipRequestDto = MentorshipRequestDto.builder()
                .requesterId(1L)
                .receiverId(2L)
                .createdAt(LocalDateTime.of(2023, 8, 30, 14, 30))
                .build();

        Locale locale = Locale.US;
        String formattedDateTime = "30-08-2023 14:30";
        String expectedMessage = String.format("Hi! Received a new request for mentoring from user id %d at %s!", mentorshipRequestDto.getRequesterId(), formattedDateTime);

        when(messageSource.getMessage(eq("mentorship.request"),
                any(Object[].class), eq(locale)))
                .thenReturn(expectedMessage);

        String actualMessage = messageBuilder.buildMessage(mentorshipRequestDto, locale);

        assertEquals(expectedMessage, actualMessage);
    }
}
