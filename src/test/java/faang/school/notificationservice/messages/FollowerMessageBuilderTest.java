package faang.school.notificationservice.messages;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerMessageBuilderTest {
    @Spy
    private MessageSource messageSource;
    @InjectMocks
    private FollowerMessageBuilder followerMessageBuilder;
    @InjectMocks
    private final UserServiceClient userServiceClient;
    private final String PROPERTIES_FILE_NAME = "follower.new";
    private final Locale locale = Locale.ENGLISH;
    private UserDto userDto;
    private FollowerEventDto followerEventDto;

    public FollowerMessageBuilderTest() {
        userServiceClient = mock(UserServiceClient.class);

    }

    @BeforeEach
    public void setUp() {
        followerEventDto = new FollowerEventDto();
        followerEventDto.setFollowerId(10L);
        followerEventDto.setFolloweeId(9L);


    }

    @Test
    public void buildMessageCallsSourceMethodTest() {
        String expectedMessage = "Congrats! You've got a new follower!";
        userDto = new UserDto(10L, "Djon",
                null, null, null, true,
                null, null, null, null, null, null,
                null, null, null, null);
        followerEventDto.setFollowerId(10L);

        when(userServiceClient.getUserInternal(10L)).thenReturn(userDto);

        when(messageSource.getMessage(PROPERTIES_FILE_NAME,
                new Object[]{userDto.username()}, locale))
                .thenReturn(expectedMessage);

        Assertions.assertEquals(expectedMessage, followerMessageBuilder.buildMessage(followerEventDto, locale));

        Mockito.verify(messageSource, Mockito.times(1))
                .getMessage(PROPERTIES_FILE_NAME, new Object[]{userDto.username()}, locale);
    }
}
