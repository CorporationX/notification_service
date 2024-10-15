package faang.school.notificationservice.service.impl;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.ProfileViewEvent;
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
class ProfileViewMessageBuilderTest {

    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ProfileViewMessageBuilder profileViewMessageBuilder;

    private ProfileViewEvent profileViewEvent;
    private UserDto profileOwnerDto;
    private UserDto profileViewerDto;

    @BeforeEach
    public void setUp() {
        profileOwnerDto = new UserDto();
        profileOwnerDto.setId(1L);
        profileOwnerDto.setUsername("ProfileOwner");

        profileViewerDto = new UserDto();
        profileViewerDto.setId(2L);
        profileViewerDto.setUsername("ProfileViewer");

        profileViewEvent = new ProfileViewEvent();
        profileViewEvent.setProfileOwnerId(profileOwnerDto.getId());
        profileViewEvent.setViewerId(profileViewerDto.getId());
    }

    @Test
    @DisplayName("Should build correct message")
    public void testBuildMessage_Success() {
        when(userServiceClient.getUser(profileOwnerDto.getId())).thenReturn(profileOwnerDto);
        when(userServiceClient.getUser(profileViewerDto.getId())).thenReturn(profileViewerDto);
        when(messageSource.getMessage(eq("profile.view"), any(Object[].class), eq(Locale.UK)))
                .thenReturn("ProfileOwner's profile was viewed by ProfileViewer");

        String message = profileViewMessageBuilder.buildMessage(profileViewEvent, Locale.UK);

        verify(userServiceClient).getUser(profileOwnerDto.getId());
        verify(userServiceClient).getUser(profileViewerDto.getId());
        verify(messageSource).getMessage(eq("profile.view"),
                eq(new Object[]{profileOwnerDto.getUsername(), profileViewerDto.getUsername()}), eq(Locale.UK));

        assertEquals("ProfileOwner's profile was viewed by ProfileViewer", message);

        verify(messageSource).getMessage(eq("profile.view"),
                argThat(args -> args[0].equals(profileOwnerDto.getUsername()) && args[1].equals(profileViewerDto.getUsername())),
                eq(Locale.UK));
    }

    @Test
    @DisplayName("Should return correct supported class")
    public void testGetSupportedClass_Success() {
        assertEquals(ProfileViewEvent.class, profileViewMessageBuilder.getSupportedClass());
    }

    @Test
    @DisplayName("Should build message for a different locale")
    public void testBuildMessage_FrenchLocale() {
        when(userServiceClient.getUser(profileOwnerDto.getId())).thenReturn(profileOwnerDto);
        when(userServiceClient.getUser(profileViewerDto.getId())).thenReturn(profileViewerDto);
        when(messageSource.getMessage(eq("profile.view"), any(Object[].class), eq(Locale.FRANCE)))
                .thenReturn("Message en français");

        String message = profileViewMessageBuilder.buildMessage(profileViewEvent, Locale.FRANCE);

        assertEquals("Message en français", message);

        verify(messageSource).getMessage(eq("profile.view"),
                argThat(args -> args[0].equals(profileOwnerDto.getUsername()) && args[1].equals(profileViewerDto.getUsername())),
                eq(Locale.FRANCE ));
    }

    @Test
    @DisplayName("Should handle exception when fetching user data")
    public void testBuildMessage_UserServiceError() {
        when(userServiceClient.getUser(profileOwnerDto.getId()))
                .thenThrow(new RuntimeException("User service error"));

        assertThrows(RuntimeException.class, () -> {
            profileViewMessageBuilder.buildMessage(profileViewEvent, Locale.UK);
        });

        verify(userServiceClient, never()).getUser(profileViewerDto.getId());
    }
}
