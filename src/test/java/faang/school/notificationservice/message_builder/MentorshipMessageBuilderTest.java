package faang.school.notificationservice.message_builder;

import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipMessageBuilderTest {
    @InjectMocks
    private MentorshipMessageBuilder mentorshipMessageBuilder;
    @Mock
    private MessageSource messageSource;
    private MentorshipAcceptedEventDto mentorshipAcceptedEventDto;

    @BeforeEach
    void setUp() {
        mentorshipAcceptedEventDto = MentorshipAcceptedEventDto.builder()
                .authorId(1L)
                .receiverId(2L)
                .authorName("author")
                .receiverName("receiver")
                .requestId(1L)
                .build();
        String text = "Congrats, " + mentorshipAcceptedEventDto.getAuthorName() + "! " + mentorshipAcceptedEventDto.getReceiverName() + " accepted your mentorship request";

        when(messageSource.getMessage(
                "mentorship.accepted",
                new Object[]{mentorshipAcceptedEventDto.getAuthorName()
                        , mentorshipAcceptedEventDto.getReceiverName()},
                Locale.UK))
                .thenReturn(text);
    }

    @Test
    void buildMessage() {
        String actual = mentorshipMessageBuilder.buildMessage(mentorshipAcceptedEventDto, Locale.UK);
        String expected = "Congrats, author! receiver accepted your mentorship request";
        assertEquals(expected, actual);
    }
}