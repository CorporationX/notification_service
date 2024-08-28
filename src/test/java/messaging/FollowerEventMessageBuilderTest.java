package messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.FollowerMessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerEventMessageBuilderTest {
    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private FollowerMessageBuilder messageBuilder;

    @Test
    public void testBuildMessage() {
        //Arrange
        FollowerEvent event = FollowerEvent.builder()
                .subscriberId(1L)
                .build();
        UserDto user = new UserDto();
        user.setUsername("testUser");
        Locale locale = Locale.ENGLISH;

        when(userServiceClient.getUser(1L)).thenReturn(user);
        when(messageSource.getMessage("follower.new", new Object[]{"testUser"}, locale)).thenReturn("User with testUser has subscribed to you.");

        //Act
        String message = messageBuilder.buildMessage(event, locale);

        //Assert
        assertEquals("User with testUser has subscribed to you.", message);
        verify(userServiceClient).getUser(1L);
        verify(messageSource).getMessage("follower.new", new Object[]{"testUser"}, locale);
    }

    @Test
    public void testBuildMessageWithException() {
        //Arrange
        FollowerEvent event = FollowerEvent.builder()
                .subscriberId(1L)
                .build();
        when(userServiceClient.getUser(
                event.getSubscriberId())).thenThrow(new RuntimeException("User service unavailable"));

        //Act & Assert
        assertThrows(RuntimeException.class, () -> messageBuilder.buildMessage(event, Locale.ENGLISH));
    }
}
