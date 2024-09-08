package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEventV2;
import faang.school.notificationservice.dto.UserDto;
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
class LikeEventMessageBuilderTest {

    @InjectMocks
    private LikeEventMessageBuilder likeEventMessageBuilder;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private MessageSource messageSource;
    private LikeEventV2 likeEvent = new LikeEventV2();
    private Locale locale = Locale.US;
    private UserDto userDto;
    private String messageCode = "like_event.new";
    private Object[] object;
    private LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        localDateTime =
                LocalDateTime.of(2020, 1, 1, 1, 1);
        likeEvent.setLikeAuthorId(1L);
        likeEvent.setLikedPostId(1L);
        likeEvent.setPostAuthorId(1L);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("User 1");
        object = new Object[]{likeEvent.getLikedPostId(), userDto.getUsername(),
                likeEvent.getLikeDateTime()};
    }

    @Test
    void shouldReturnTextMessageWhenBuildMessageTest() {

        //arrange
        String message = "Your post " + likeEvent.getLikedPostId() +
                "was liked by user " + userDto.getUsername() + "at " + localDateTime;
        //act
        when(userServiceClient.getUser(1L)).thenReturn(userDto);
        when(messageSource.getMessage(messageCode, object, locale))
                .thenReturn(message);

        //assert
        assertEquals(message, likeEventMessageBuilder.buildMessage(likeEvent, locale));
    }
}