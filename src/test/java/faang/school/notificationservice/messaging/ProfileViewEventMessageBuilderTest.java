package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEventDto;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventMessageBuilderTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MessageSource messageSource;

    private ProfileViewEventMessageBuilder messageBuilder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        messageBuilder = new ProfileViewEventMessageBuilder(userServiceClient, messageSource);
    }

    @Test
    public void testBuildMessage() {
        // Arrange
        ProfileViewEventDto event = new ProfileViewEventDto();
        event.setViewerId(1L);
        UserDto user = new UserDto();
        user.setUsername("testUser");
        Locale locale = Locale.US;

        when(userServiceClient.getUser(1L)).thenReturn(user);
        when(messageSource.getMessage("profile.view", new Object[]{"testUser"}, locale)).thenReturn("User testUser viewed the profile");

        // Act
        String message = messageBuilder.buildMessage(event, locale);

        // Assert
        assertEquals("User testUser viewed the profile", message);
        verify(userServiceClient).getUser(1L);
        verify(messageSource).getMessage("profile.view", new Object[]{"testUser"}, locale);
    }

    @Test
    public void testGetInstance() {
        // Act
        Class<ProfileViewEventDto> instanceClass = messageBuilder.getInstance();

        // Assert
        assertEquals(ProfileViewEventDto.class, instanceClass);
    }
}
