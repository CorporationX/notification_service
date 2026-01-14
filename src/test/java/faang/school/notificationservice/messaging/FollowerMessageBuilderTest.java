package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerMessageBuilderTest {

    @InjectMocks
    private FollowerMessageBuilder messageBuilder;

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserServiceClient userServiceClient;

    @Test
    void shouldBuildMessage() {
        FollowerEvent event = new FollowerEvent(1L, 2L, LocalDateTime.now());
        Locale locale = Locale.ENGLISH;

        UserDto follower = new UserDto();
        follower.setId(2L);
        follower.setUsername("follower");

        when(userServiceClient.getUser(2L)).thenReturn(follower);

        String mockedMessage = "mocked message";

        when(messageSource.getMessage(
                eq("follower.new"),
                any(),
                eq(Locale.ENGLISH)
        )).thenReturn(mockedMessage);

        String builtMessage = messageBuilder.buildMessage(event, locale);

        assertEquals(mockedMessage, builtMessage);

        verify(messageSource).getMessage(
                eq("follower.new"),
                any(),
                eq(Locale.ENGLISH)
        );
    }
}
