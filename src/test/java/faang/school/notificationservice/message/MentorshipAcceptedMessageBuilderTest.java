package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.MentorshipAcceptedEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedMessageBuilderTest {
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private MentorshipAcceptedMessageBuilder mentorshipAcceptedMessageBuilder;

    @Test
    void testBuildMessage() {
        //Arrange
        MentorshipAcceptedEventDto event = new MentorshipAcceptedEventDto();
        Locale locale = Locale.getDefault();
        //Act
        mentorshipAcceptedMessageBuilder.buildMessage(event, locale);
        //Assert
        Mockito.verify(messageSource, Mockito.times(1)).getMessage(any(), any(), any());
    }

    @Test
    void testSupportsEventType() {
        //Act
        Class<?> result = mentorshipAcceptedMessageBuilder.supportsEventType();
        //Assert
        assertEquals(result, MentorshipAcceptedEventDto.class);
    }
}