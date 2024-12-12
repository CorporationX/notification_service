package faang.school.notificationservice.messaging.profile;

import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileViewMessageBuilderTest {

    @InjectMocks
    private ProfileViewMessageBuilder profileViewMessageBuilder;

    @Mock
    private MessageSource messageSource;

    @Test
    void testBuildMessage() {
        ProfileViewEvent mockEvent = ProfileViewEvent.builder().build();

        String expectedMessage = "User 123 viewed the profile at 2024-07-18T12:00:00Z";
        when(messageSource.getMessage(any(), any(), any()))
                .thenReturn(expectedMessage);

        String actualMessage = profileViewMessageBuilder.buildMessage(mockEvent, Locale.UK);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetInstance() {
        Class<?> actual = profileViewMessageBuilder.getInstance();
        assertEquals(actual, ProfileViewEvent.class);
    }

}