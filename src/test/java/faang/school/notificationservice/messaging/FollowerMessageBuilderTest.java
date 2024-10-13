package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerMessageBuilderTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private FollowerMessageBuilder followerMessageBuilder;

    private final String username = "testUser";
    private final Locale locale = Locale.ENGLISH;
    private final LocalDateTime eventTime = LocalDateTime.of(2024, 10, 13, 15, 30);

    private UserDto user;
    private FollowerEvent event;

    @BeforeEach
    public void setUp() {
        user = new UserDto();
        user.setUsername(username);
        user.setLocale(locale);

        event = new FollowerEvent();
        event.setEventTime(eventTime);
    }

    @Test
    public void testBuildMessage() {
        String textAddress = "follower.new";
        Object[] args = {username, eventTime.toLocalTime()};
        String correctResult = "User testUser followed you at 15:30";
        when(messageSource.getMessage(textAddress, args, locale)).thenReturn(correctResult);

        String result = followerMessageBuilder.buildMessage(user, event);

        assertEquals(correctResult, result);
    }

    @Test
    public void testGetInstance() {
        assertEquals(FollowerEvent.class, followerMessageBuilder.getInstance());
    }
}
