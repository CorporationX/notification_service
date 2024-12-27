package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.ProfileViewEvent;
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
class ViewMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ViewMessageBuilder viewMessageBuilder;

    @Test
    void testBuildMessage() {
        Long viewerId = 1L;
        Long viewedId = 2L;
        ProfileViewEvent event = new ProfileViewEvent(viewerId, viewedId);
        Locale locale = Locale.ENGLISH;

        when(messageSource.getMessage("profile.view", new Object[]{viewerId, viewedId}, locale))
                .thenReturn("User with id: 1 viewed your profile.");

        String result = viewMessageBuilder.buildMessage(event, locale);

        assertEquals("User with id: 1 viewed your profile.", result);
    }

    @Test
    void testGetInstance() {
        Class<?> result = viewMessageBuilder.getInstance();

        assertEquals(ProfileViewEvent.class, result);
    }
}