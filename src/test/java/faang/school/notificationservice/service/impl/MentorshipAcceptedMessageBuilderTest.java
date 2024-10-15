package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.MentorshipAcceptedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipAcceptedMessageBuilderTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MentorshipAcceptedMessageBuilder mentorshipAcceptedMessageBuilder;

    private MentorshipAcceptedEvent mentorshipAcceptedEvent;
    private UserDto requesterDto;
    private UserDto mentorDto;

    @BeforeEach
    public void setUp() {
        requesterDto = new UserDto();
        requesterDto.setId(1L);
        requesterDto.setUsername("Requester");

        mentorDto = new UserDto();
        mentorDto.setId(2L);
        mentorDto.setUsername("Mentor");

        mentorshipAcceptedEvent = new MentorshipAcceptedEvent();
        mentorshipAcceptedEvent.setRequesterId(requesterDto.getId());
        mentorshipAcceptedEvent.setMentorId(mentorDto.getId());
        mentorshipAcceptedEvent.setRequestId(108L);
    }

    @Test
    @DisplayName("Should build correct message")
    public void testBuildMessage_Success() {
        when(userServiceClient.getUser(requesterDto.getId())).thenReturn(requesterDto);
        when(userServiceClient.getUser(mentorDto.getId())).thenReturn(mentorDto);
        when(messageSource.getMessage(eq("mentorship.accepted"), any(Object[].class), eq(Locale.UK)))
                .thenReturn("Requester's mentorship request was accepted by Mentor");

        String message = mentorshipAcceptedMessageBuilder.buildMessage(mentorshipAcceptedEvent, Locale.UK);

        verify(userServiceClient).getUser(requesterDto.getId());
        verify(userServiceClient).getUser(mentorDto.getId());
        verify(messageSource).getMessage(eq("mentorship.accepted"), any(Object[].class), eq(Locale.UK));

        assertEquals("Requester's mentorship request was accepted by Mentor", message);

        verify(messageSource).getMessage(eq("mentorship.accepted"),
                argThat(args -> args[0].equals(requesterDto.getUsername())
                        && args[1].equals(mentorshipAcceptedEvent.getRequestId())
                        && args[2].equals(mentorDto.getUsername())),
                eq(Locale.UK));
    }

    @Test
    @DisplayName("Should return correct supported class")
    public void testGetSupportedClass_Success() {
        assertEquals(MentorshipAcceptedEvent.class, mentorshipAcceptedMessageBuilder.getSupportedClass());
    }

    @Test
    @DisplayName("Should build message for a different locale")
    public void testBuildMessage_FrenchLocale() {
        when(userServiceClient.getUser(requesterDto.getId())).thenReturn(requesterDto);
        when(userServiceClient.getUser(mentorDto.getId())).thenReturn(mentorDto);
        when(messageSource.getMessage(eq("mentorship.accepted"), any(Object[].class), eq(Locale.FRANCE)))
                .thenReturn("Message en français");

        String message = mentorshipAcceptedMessageBuilder.buildMessage(mentorshipAcceptedEvent, Locale.FRANCE);

        assertEquals("Message en français", message);

        verify(messageSource).getMessage(eq("mentorship.accepted"),
                argThat(args -> args[0].equals(requesterDto.getUsername())
                        && args[1].equals(mentorshipAcceptedEvent.getRequestId())
                        && args[2].equals(mentorDto.getUsername())),
                eq(Locale.FRANCE ));
    }

    @Test
    @DisplayName("Should handle exception when fetching user data")
    public void testBuildMessage_UserServiceError() {
        when(userServiceClient.getUser(requesterDto.getId()))
                .thenThrow(new RuntimeException("User service error"));

        assertThrows(RuntimeException.class, () ->
                mentorshipAcceptedMessageBuilder.buildMessage(mentorshipAcceptedEvent, Locale.UK));

        verify(userServiceClient).getUser(requesterDto.getId());
        verify(userServiceClient, never()).getUser(mentorDto.getId());
    }
}
