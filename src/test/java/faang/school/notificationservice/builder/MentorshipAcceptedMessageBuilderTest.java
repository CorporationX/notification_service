package faang.school.notificationservice.builder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import faang.school.notificationservice.dto.UserDto;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedMessageBuilderTest {

    private static final long MENTEE_ID = 1L;
    private static final long MENTOR_ID = 2L;
    private static final long REQUEST_ID = 3L;
    private static final String USERNAME = "Mentor User Name";
    private static final String EMAIL = "test@gmail.com";
    private static final String MSG_EVENT_KEY = "mentorship_accepted";
    private static final String LOCALE = "ENGLISH";

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipAcceptedMessageBuilder mentorshipAcceptedMessageBuilder;

    @Test
    @DisplayName("When message build")
    public void whenBuildMessageOk() {
        String expectedResult = USERNAME + " accepted your request " + REQUEST_ID + " for mentorship";

        UserDto mentorDto = UserDto.builder()
                .id(MENTOR_ID)
                .username(USERNAME)
                .email(EMAIL)
                .preference(UserDto.PreferredContact.EMAIL)
                .build();

        UserDto menteeDto = UserDto.builder()
                .id(MENTEE_ID)
                .username(USERNAME)
                .email(EMAIL)
                .preference(UserDto.PreferredContact.EMAIL)
                .preferredLocale(LOCALE)
                .build();

        MentorshipAcceptedEvent event = MentorshipAcceptedEvent.builder()
                .actorId(MENTEE_ID)
                .receiverId(MENTOR_ID)
                .requestId(REQUEST_ID)
                .build();

        when(userServiceClient.getUser(MENTOR_ID)).thenReturn(mentorDto);
        when(userServiceClient.getUser(MENTEE_ID)).thenReturn(menteeDto);
        when(messageSource.getMessage(MSG_EVENT_KEY, new Object[]{USERNAME, REQUEST_ID}, Locale.ENGLISH)).thenReturn(expectedResult);

        String actualResult = mentorshipAcceptedMessageBuilder.buildMessage(event);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("When message not build. User not found")
    public void whenBuildMessageUserNotFound() {

        MentorshipAcceptedEvent event = MentorshipAcceptedEvent.builder()
                .actorId(MENTEE_ID)
                .receiverId(MENTOR_ID)
                .requestId(REQUEST_ID)
                .build();

        FeignException exception = Mockito.mock(FeignException.class);
        Mockito.when(exception.status()).thenReturn(404);
        when(userServiceClient.getUser(MENTEE_ID)).thenThrow(exception);

        var actualException = assertThrows(FeignException.class, () -> mentorshipAcceptedMessageBuilder.buildMessage(event));
        assertEquals(actualException.status(), 404);
    }
}