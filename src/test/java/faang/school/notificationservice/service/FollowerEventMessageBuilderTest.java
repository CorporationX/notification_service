package faang.school.notificationservice.service;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.FollowerEventDto;
import faang.school.notificationservice.messaging.FollowerEventMessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerEventMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private FollowerEventMessageBuilder followerEventMessageBuilder;

    @Test
    void testGetInstance() {
        assertEquals(FollowerEventDto.class, followerEventMessageBuilder.getInstance());
    }

    @Test
    void testBuildMessageSuccess() {
        final FollowerEventDto event = new FollowerEventDto(1L, 2L, null);
        UserDto follower = new UserDto();
        follower.setUsername("TestFollower");
        Locale locale = Locale.ENGLISH;
        String expectedMessage = "New follower: TestFollower";

        when(userServiceClient.getUser(1L)).thenReturn(follower);
        when(messageSource.getMessage("event.newFollower", new Object[]{"TestFollower"}, locale))
                .thenReturn(expectedMessage);

        String actualMessage = followerEventMessageBuilder.buildMessage(event, locale);
        assertEquals(expectedMessage, actualMessage);

        verify(userServiceClient).getUser(1L);
        verify(messageSource).getMessage("event.newFollower", new Object[]{"TestFollower"}, locale);
    }

    @Test
    void testBuildMessageUserNotFound() {
        final FollowerEventDto event = new FollowerEventDto(1L, 2L, null);
        Locale locale = Locale.ENGLISH;
        String expectedMessage = "New follower: Unknown User";

        when(userServiceClient.getUser(1L)).thenReturn(null);
        when(messageSource.getMessage("event.newFollower", new Object[]{"Unknown User"}, locale))
                .thenReturn(expectedMessage);

        String actualMessage = followerEventMessageBuilder.buildMessage(event, locale);
        assertEquals(expectedMessage, actualMessage);

        verify(userServiceClient).getUser(1L);
        verify(messageSource).getMessage("event.newFollower", new Object[]{"Unknown User"}, locale);
    }
}